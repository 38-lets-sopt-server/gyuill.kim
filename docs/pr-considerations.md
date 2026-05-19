# PR 고민 내용 정리

## 1. 애플리케이션 결과 매퍼 분리

### 배경

`PostCommandService`와 `PostQueryService`에서 `Post` 엔티티를 `PostResult`로 변환하는 코드가 중복되어 있었습니다. 동일한 필드를 직접 꺼내 `new PostResult(...)`를 만드는 구조라, 응답 필드가 변경될 때 두 서비스가 함께 수정되어야 했습니다.

### 검토한 선택지

- `PostResult`에 정적 팩토리 메서드 추가
- 별도 Spring Bean 매퍼 추가
- 순수 유틸 매퍼 클래스 추가
- `Post` 엔티티에 변환 메서드 추가

### 결정

정적 팩토리 메서드는 DTO가 도메인 엔티티를 알게 되는 형태라 선호하지 않았고, Spring Bean 매퍼는 의존성이 없는 단순 변환에 비해 과하다고 판단했습니다.

그래서 `application.mapper.PostResultMapper`를 추가해 `Post -> PostResult` 변환을 한 곳으로 모았습니다.

```java
PostResultMapper.toResult(post)
```

같은 이유로 `UserCommandService`와 `UserQueryService`에 중복되어 있던 `User -> UserResult` 변환도 `application.mapper.UserResultMapper`로 분리했습니다.

### 의도

- command/query 서비스에서 변환 중복 제거
- 도메인 엔티티가 애플리케이션 DTO를 알지 않도록 유지
- DTO에 정적 팩토리 메서드를 두지 않음
- 의존성이 없는 단순 변환이므로 Spring Bean으로 만들지 않음

## 2. Presentation 응답 매퍼는 유지

`PostResponseMapper`, `UserResponseMapper`는 유지했습니다.

`Post -> PostResult`, `User -> UserResult` 변환은 애플리케이션 계층 내부 반환 모델을 만드는 책임입니다. 반면 `PostResult -> PostResponse`, `UserResult -> UserResponse` 변환은 프레젠테이션 계층의 API 응답 모델을 만드는 책임입니다.

따라서 응답 매퍼는 컨트롤러의 응답 조립을 줄이고 계층 경계를 명확히 하는 역할이 있다고 보고 유지했습니다.

## 3. Request -> Command 변환은 보류

컨트롤러에서 `CreatePostRequest -> CreatePostCommand`, `UpdatePostRequest -> UpdatePostCommand` 같은 변환을 직접 하고 있습니다.

이 부분도 매퍼로 분리할 수 있지만, 현재는 한두 줄 수준이라 별도 추상화를 추가하는 이점이 크지 않다고 판단했습니다. 요청 DTO가 늘어나거나 컨트롤러가 더 복잡해지면 presentation mapper로 분리할 수 있습니다.

## 4. `@ModelAttribute` 사용

게시글 목록 조회와 검색 API는 `GET` 요청의 query parameter를 DTO로 받기 위해 `@ModelAttribute`를 사용했습니다.

예를 들어 아래 요청은:

```http
GET /posts?boardType=FREE&cursor=10&size=20
```

Spring MVC에 의해 `GetPostsRequest`로 바인딩됩니다.

```java
@Valid @ModelAttribute GetPostsRequest request
```

이는 여러 `@RequestParam`을 개별로 받는 대신 관련 조회 조건을 하나의 요청 DTO로 묶기 위한 선택입니다. `@Valid`도 함께 적용되어 `size`, `cursor` 같은 query parameter 검증이 동작합니다.

`GetPostsRequest`와 `SearchPostsRequest`는 모두 실제 컨트롤러에서 사용 중입니다.

## 5. 페이징 기본값 중복은 보류

`GetPostsRequest`와 `SearchPostsRequest` 모두 `size == null`이면 기본값 `10`을 적용합니다.

```java
public GetPostsRequest {
    if (size == null) {
        size = 10;
    }
}
```

현재는 DTO가 2개뿐이라 별도 공통 추상화를 만들지 않았습니다. 페이징 요청 DTO가 더 늘어나면 공통 상수나 별도 요청 모델로 분리하는 것을 검토할 수 있습니다.

## 6. 삭제 응답과 공통 응답 포맷

현재 삭제 성공 코드는 `HttpStatus.NO_CONTENT`를 사용합니다.

`CommonApiResponse.successResponse()`는 성공 코드가 `204 No Content`이면 body 없이 응답하도록 처리합니다.

```java
if (successCode.getHttpStatus() == HttpStatus.NO_CONTENT) {
    return ResponseEntity.noContent().build();
}
```

### 검토한 선택지

- REST 관점 유지: 삭제는 `204 No Content`, body 없음
- 공통 응답 포맷 우선: 삭제도 `200 OK`, 공통 body 반환

### 결정

REST 관점을 유지해 삭제 API는 `204 No Content`로 두기로 했습니다. 다만 이 경우 "모든 API 응답을 공통 포맷으로 감싼다"는 설명에서 삭제 API는 예외가 됩니다.

필요하다면 PR 설명에서 `204 No Content` 응답은 HTTP 규격에 맞춰 body를 반환하지 않는다고 명시하면 됩니다.

## 7. 게시글 정책 검증 객체

`PostContentPolicyValidator`는 게시글 제목/본문에 대해 다음 정책을 검사합니다.

- 위험 HTML/XSS 패턴
- 개인정보로 추정되는 패턴

정책 위반 시 예외를 던지지 않고 `PostModerationResult`를 반환해 게시글을 `HIDDEN` 상태로 전환할지 판단합니다.

### 판단

현재 정책이 2개뿐이라 rule 인터페이스나 여러 validator로 더 쪼개는 것은 아직 이르다고 판단했습니다.

예를 들어 아래와 같은 구조는 확장성은 있지만 현재 규모에서는 코드 양이 과하게 늘어납니다.

```java
interface PostPolicyRule {
    Optional<String> findViolationReason(String title, String content);
}
```

정책이 더 늘어나거나, 정책별 활성화/비활성화, 우선순위, 외부 설정 관리가 필요해지면 그때 rule 기반 구조로 분리하는 것이 적절합니다.

### 향후 개선 여지

- `Validator`라는 이름이 예외 기반 검증처럼 보일 수 있어 `PostContentPolicyChecker` 또는 `PostContentModerator` 같은 이름도 고려 가능
- 긴 주석은 핵심 의도만 남기고 README 또는 별도 문서로 이동 가능
- 숨김 사유 문자열은 정책 결과로 남는 값이므로 상수화 가능

## 8. QueryDSL 조회 중복 개선 범위

`PostQueryRepositoryImpl`의 목록 조회와 검색 쿼리는 QueryDSL의 `BooleanExpression`을 사용하고 있습니다.

```java
.where(
        publishedPost(),
        boardTypeEq(boardType),
        postIdLt(cursor)
)
```

`boardTypeEq(...)`, `postIdLt(...)`, `keywordContainedInTitleOrContent(...)`는 조건이 없으면 `null`을 반환하고, QueryDSL은 `where()`의 `null` 조건을 무시합니다. 따라서 동적 조건 자체는 이미 활용하고 있다고 판단했습니다.

다만 `Slice` 생성 로직은 목록 조회와 검색에서 동일하게 반복되고 있었습니다.

```java
boolean hasNext = posts.size() > size;
List<Post> content = hasNext
        ? new ArrayList<>(posts.subList(0, size))
        : posts;
return new SliceImpl<>(content, PageRequest.of(0, size), hasNext);
```

이 부분은 커서 기반 조회 결과를 Spring `Slice`로 변환하는 공통 규칙이므로 `toSlice(posts, size)` 메서드로 분리했습니다.

쿼리 본문 자체를 공통 fetch 메서드로 추출하는 것은 보류했습니다. 현재는 두 쿼리의 구조가 비슷하지만, 목록 조회와 검색은 향후 정렬 기준, 검색 대상, 랭킹 조건 등이 달라질 수 있으므로 조회 목적별 메서드에 남겨두는 편이 더 읽기 쉽다고 판단했습니다.

## 9. 트랜잭션 적용 범위 고민

현재 서비스는 CQRS 의도를 드러내기 위해 command/query 서비스에 클래스 단위 트랜잭션을 적용하고 있습니다.

```java
@Transactional
public class PostCommandService {
}

@Transactional(readOnly = true)
public class PostQueryService {
}
```

### 고민한 지점

카카오페이 기술 블로그의 "JPA Transactional 잘 알고 쓰고 계신가요?" 글을 참고했습니다.

해당 글의 핵심은 `@Transactional(readOnly = true)`도 실제 DB 입장에서는 트랜잭션 시작, read-only 설정, commit, autocommit 설정 같은 부가 작업을 만들 수 있고, 고트래픽 조회 API에서는 이 비용이 유의미할 수 있다는 점입니다.

따라서 "조회 서비스 전체에 클래스 단위로 read-only 트랜잭션을 거는 것이 항상 최선인가?"를 검토했습니다.

### 선택지

#### 1. 클래스 단위 트랜잭션 유지

```java
@Transactional(readOnly = true)
public class PostQueryService {
}
```

장점:

- 조회 서비스의 모든 public 메서드가 read-only라는 의도가 명확함
- 메서드마다 어노테이션을 반복하지 않아 코드가 단순함
- OSIV가 꺼져 있어도 서비스 메서드 안에서는 영속성 컨텍스트가 안정적으로 유지됨
- Lazy loading 또는 entity graph/fetch join 누락이 있을 때 장애 가능성이 낮음

단점:

- 단순 조회에도 항상 트랜잭션이 생성될 수 있음
- MySQL 환경에서는 read-only 트랜잭션 설정, commit, autocommit 관련 부가 쿼리가 생길 수 있음
- 고트래픽 조회 API에서는 DB 리소스 사용량이 불필요하게 늘 수 있음
- 클래스 단위 적용이라 일부 메서드에서 트랜잭션이 필요 없어도 자동 적용됨

#### 2. QueryService 클래스 단위 제거, 필요한 메서드에만 적용

```java
public class PostQueryService {

    public PostCursorResult getPosts(...) {
    }

    @Transactional(readOnly = true)
    public PostResult getPost(...) {
    }
}
```

장점:

- 단순 조회 메서드에서는 트랜잭션 비용을 줄일 수 있음
- 메서드마다 트랜잭션 필요 여부를 명시적으로 판단하게 됨
- 고트래픽 조회 API에서 불필요한 DB transaction 부가 작업을 줄일 여지가 있음

단점:

- 어떤 메서드에 트랜잭션이 필요한지 계속 판단해야 함
- fetch join/entity graph가 빠진 조회가 생기면 OSIV가 꺼진 환경에서 LazyInitializationException이 발생할 수 있음
- 메서드별 어노테이션 정책이 섞이면 오히려 컨벤션이 흐려질 수 있음
- 현재 과제 규모에서는 성능 이득을 측정하기 어렵고, 코드 복잡도만 늘 수 있음

#### 3. read-only 트랜잭션에 `Propagation.SUPPORTS` 적용

```java
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public @interface ReadOnlyTransactional {
}
```

장점:

- 단독 실행 시 새 트랜잭션을 만들지 않고 조회할 수 있음
- 상위 트랜잭션이 있으면 자연스럽게 참여함
- read-only 조회 정책을 커스텀 어노테이션으로 통일할 수 있음

단점:

- 프로젝트 규모에 비해 커스텀 트랜잭션 어노테이션이 과할 수 있음
- 팀 전체 컨벤션과 테스트가 함께 필요함
- JPA/Spring Data 기본 repository 메서드의 자체 트랜잭션과 맞물리는 동작을 별도로 확인해야 함
- 과제 코드에서는 성능 이득을 실제로 검증하기 어렵고 설명 비용이 큼

### 현재 코드 기준 판단

`CommandService`의 클래스 단위 `@Transactional`은 유지하는 것이 적절하다고 판단했습니다.

이유:

- `createPost()`는 `save()` 이후 검수 결과에 따라 상태 변경을 같은 작업 단위로 묶어야 함
- `updatePost()`, `deletePost()`는 엔티티 조회 후 dirty checking으로 변경을 반영함
- 반응 토글은 트랜잭션 경계가 달라야 하므로 이미 `NOT_SUPPORTED`, `REQUIRES_NEW`로 별도 처리 중

반면 `QueryService`의 클래스 단위 `@Transactional(readOnly = true)`는 운영 성능 관점에서 재검토 여지가 있습니다.

다만 현재 프로젝트에서는 다음 이유로 당장 제거하지 않았습니다.

- 과제 규모에서 read-only 트랜잭션 부가 비용을 실제로 측정하기 어려움
- CQRS 구조에서 query 서비스 전체가 read-only라는 의도가 명확하게 드러남
- `open-in-view: false` 환경에서 서비스 내부 조회/검증/변환 범위를 안정적으로 유지할 수 있음
- 게시글 조회는 fetch join/entity graph를 사용하지만, 향후 조회가 추가될 때 트랜잭션 제거가 실수로 이어질 수 있음

### 최종 정리

현재 PR에서는 클래스 단위 트랜잭션을 유지합니다.

다만 운영 성능을 목표로 개선한다면 다음 순서가 적절하다고 판단했습니다.

1. 조회 API의 실제 트래픽과 DB general log를 확인한다.
2. `@Transactional(readOnly = true)`로 인한 set option, commit, autocommit 관련 쿼리 비중을 측정한다.
3. 병목이 확인되면 QueryService부터 클래스 단위 트랜잭션을 제거한다.
4. 단순 조회는 트랜잭션 없이 유지하고, 영속성 컨텍스트가 필요한 조회에만 메서드 단위 read-only 트랜잭션을 붙인다.
5. 조회용 커스텀 어노테이션이 필요할 만큼 규칙이 반복되면 `@ReadOnlyTransactional` 같은 컨벤션을 도입한다.

PR 답변에는 다음처럼 정리할 수 있습니다.

```text
readOnly 트랜잭션도 DB에 부가 쿼리를 만들 수 있다는 점은 타당하다고 보고, 운영 성능 관점에서는 QueryService의 클래스 단위 트랜잭션을 줄이는 선택지도 검토했습니다.
다만 현재 command 서비스는 save 이후 상태 변경과 dirty checking이 필요하므로 트랜잭션을 유지하는 것이 맞다고 판단했습니다.
query 서비스는 fetch join/entity graph로 필요한 연관을 명시적으로 로딩하고 있어 향후 트래픽과 DB 로그를 기준으로 클래스 단위 readOnly 트랜잭션 제거를 검토할 수 있습니다.
이번 PR에서는 과제 규모와 CQRS 의도 표현, OSIV off 환경에서의 안정성을 우선해 클래스 단위 트랜잭션을 유지했습니다.
```

## 10. 현재까지 적용한 커밋

- `refactor: 게시글 결과 매퍼 분리`
- `refactor: 사용자 결과 매퍼 분리`

## 11. 인증/인가 리뷰 후 결정 사항

### 11.1 사용자 수정/삭제 API 인가

리뷰에서 `PATCH /users/{userId}`, `DELETE /users/{userId}`가 로그인 여부만 확인하고, 토큰 사용자와 path variable의 대상 사용자가 같은지 확인하지 않는 문제가 지적되었습니다.

선택지는 두 가지였습니다.

- 기존 경로를 유지하고 `requesterUserId == targetUserId`를 서비스에서 검증
- API 계약을 바꿔 `PATCH /users/me`, `DELETE /users/me`로 전환

현재 판단은 두 번째 방향입니다.

이유:

- 클라이언트가 수정/삭제 대상 ID를 임의로 지정하지 못함
- "내 계정 수정/삭제"라는 API 의미가 더 명확함
- 관리자용 사용자 수정/삭제가 필요해지면 `/admin/users/{userId}`처럼 별도 API로 분리하기 쉬움
- 게시글 작성자 검증처럼 리소스 소유권을 서비스에서 확인하는 방식도 가능하지만, 사용자 자기 자신에 대한 명령은 `/me`가 더 단순함

다만 이 변경은 API 계약을 바꾸므로 별도 작업으로 분리합니다.

### 11.2 숨김 게시글 조회 API 정책

`GET /posts/{postId}/hidden`은 현재 인증된 사용자라면 접근할 수 있습니다. 기존 주석에도 일반 사용자에게 열 API가 아니라는 점이 적혀 있었고, 인증/인가 도입 후에는 정책을 명확히 해야 합니다.

검토한 선택지는 다음과 같습니다.

- API 제거
- 작성자 본인만 허용
- 관리자 전용으로 제한

최종 방향은 관리자 전용입니다.

이유:

- 숨김 게시글은 운영/검수 대상에 가깝고 일반 사용자 기능으로 보기 어려움
- 작성자에게 숨김 게시글을 보여줄 필요가 있다면 별도 사용자 시나리오와 응답 정책이 필요함
- 현재 프로젝트에는 Role/Authority 모델이 없으므로 지금 당장 관리자 전용 인가를 구현하면 범위가 커짐

따라서 이번 작업에서는 TODO로 남기고, 관리자 권한 모델을 도입할 때 `GET /posts/{postId}/hidden`을 관리자 전용으로 제한합니다.

### 11.3 Refresh Token 저장 방식

처음 구현은 refresh token 원문을 DB에 저장했습니다. 이 방식은 구현이 단순하지만 DB가 유출되면 refresh token 자체가 재사용 가능한 인증 수단이 됩니다.

검토한 선택지는 다음과 같습니다.

- 원문 저장
- SHA-256 해시 저장
- 서버 secret 기반 HMAC-SHA256 저장

최종 방향은 HMAC-SHA256 저장입니다.

이유:

- DB에는 refresh token 원문이 남지 않음
- 단순 SHA-256보다 서버 secret 없이는 같은 저장값을 재계산하기 어려움
- 비밀번호를 BCrypt로 저장하는 것처럼 인증 수단 원문을 저장하지 않는 원칙과 맞음
- 재발급 요청 시 요청 token을 HMAC 처리한 뒤 저장된 HMAC 값과 비교할 수 있음

구현 방향:

- `RefreshToken` 엔티티는 `token` 원문 대신 `tokenHash`만 저장
- `RefreshTokenHasher`가 HMAC-SHA256 해시 생성을 담당
- 비교는 `MessageDigest.isEqual()`로 constant-time 비교
- JWT 서명 secret과 refresh token 저장용 HMAC secret은 분리

### 11.4 Refresh Token 재발급 동시성

현재 재발급 흐름은 다음 순서입니다.

1. refresh token JWT 검증
2. 사용자 조회
3. 저장된 refresh token hash 조회
4. 요청 token hash와 저장 hash 비교
5. 새 access/refresh token 발급
6. 저장된 refresh token hash 교체

동일한 refresh token으로 동시 재발급 요청이 들어오면 둘 다 기존 hash 검증을 통과할 여지가 있습니다.

정석적인 보강 방향은 다음 중 하나입니다.

- `RefreshToken`에 `@Version`을 추가해 낙관적 락으로 한 요청만 성공하게 처리
- refresh token row를 `PESSIMISTIC_WRITE`로 조회
- `where token_hash = ?` 조건 기반 원자적 update로 rotation을 보장

현재 코드 규모와 기존 게시글 반응 처리에서 낙관적 락을 이미 사용한 점을 고려하면, 다음 단계에서는 `@Version` 기반 보강이 가장 자연스럽습니다.

다만 이번 작업에서는 원문 저장 제거를 우선 적용하고, 재발급 동시성 보강은 별도 작업으로 분리합니다.

### 11.5 JWT 라이브러리 전환

기존 `JwtTokenProvider`는 외부 JWT 라이브러리 없이 HMAC-SHA256 JWT를 직접 생성/검증했습니다.

학습 목적에서는 내부 동작을 드러내는 장점이 있지만, 운영 관점에서는 검증된 JWT 라이브러리를 사용하는 것이 더 적절하다고 판단했습니다.

직접 구현을 유지하면 다음 항목을 계속 직접 책임져야 합니다.

- signature 비교 constant-time 처리
- secret 길이 검증
- issuer/audience claim 검증
- clock skew 허용
- 표준 claim 직렬화/역직렬화와 만료 검증

최종 방향은 JJWT 전환입니다.

적용 내용:

- `io.jsonwebtoken:jjwt-*` 의존성 추가
- JWT 서명/검증/claim 파싱을 JJWT에 위임
- `issuer` 검증 추가
- `clockSkewSeconds` 설정 추가
- HS256 secret 길이 검증은 JJWT의 `Keys.hmacShaKeyFor()`와 `WeakKeyException`에 위임
- `typ` claim으로 access/refresh token 타입 검증 유지
- `jti` claim 유지

또한 토큰 응답에 만료 시각을 포함하도록 API 응답을 확장했습니다.

이유:

- 클라이언트가 access token 만료 전 재발급 시점을 판단할 수 있음
- refresh token 만료 시각도 세션 처리에 활용할 수 있음
- 서버는 토큰 발급 시 만료 시각을 이미 알고 있으므로 응답 모델에 포함하는 것이 자연스러움
