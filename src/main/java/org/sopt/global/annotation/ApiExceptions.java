package org.sopt.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 예외 예시 생성을 위해 컨트롤러 메서드에 연결하는 커스텀 어노테이션입니다.
 * 도메인별 에러 코드 enum을 선언하면 문서화 계층에서 상태 코드별 예시 응답을 자동으로 조립합니다
 *
 * 이건 제가 프로젝트 시작 할 때마다 일단 설정해두고 시작하는 템플릿인데요. 스웨거의 가장 큰 단점이라면 코드가 더러워진다는 건데요.
 * 그래서 어노테이션을 통해서 코드를 지저분하게 만드는 요소들을 최대한 줄이려고 합니다.
 *
 * 근데 이것도 협헙하면서 느끼는건데 이렇게 만든 커스텀 어노테이션을 만들고 컨벤션으로써 활용하는게 되겍 어려운 일이더라고요,
 * 그래도 이걸 안쓰면 코드가 너무 더러워지니까 소통비용보다 효과가 더 크다고 생각해서 선호하는 편입니다.
 *
 * 물론 이 형태도 타협을 한 형태인데 각 엔드포인트멸로 정확한 에러코드만 보여줄 수는 없다는 문제가 있는데요.
 * 정확한 엔드포인트만 보여주게 만들면 복잡도가 많이 올라가고 결국 실제로 컨트롤러에 붙이는 코드도 다시 지저분해져서 어노테이션을 만드는 이유가 희석되더라고요.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiExceptions {

    /**
     * 문서화 대상 에러 코드 enum 목록.
     *
     * @return 에러 코드 enum 배열
     */
    Class<? extends Enum<?>>[] value();
}
