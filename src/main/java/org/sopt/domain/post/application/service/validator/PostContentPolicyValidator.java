package org.sopt.domain.post.application.service.validator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 과제 범위에 맞춘 가벼운 게시글 내용 정책 검사기.
 * 정교한 차단 시스템 대신 숨김 전환 여부만 판단해 운영 의도를 코드에 남긴다.
 */
@Component
public class PostContentPolicyValidator {
    private static final List<String> DANGEROUS_PATTERNS = List.of(
            "<script",
            "javascript:",
            "<iframe",
            "<object",
            "<embed",
            "onload=",
            "onclick=",
            "onerror="
    );

    /**
     * 게시글 본문/제목에서 우선 차단할 개인정보 패턴 최소 목록.
     * - 휴대전화 번호: `010-1234-5678`, `01012345678`
     * - 이메일 주소: `user@example.com`
     * - 하이픈 포함 숫자 식별값: 계좌번호/유선전화/기타 숫자 구분값의 단순 패턴
     * - 연속 숫자 10~14자리: 하이픈 없이 입력된 전화번호/계좌번호 가능성
     */
    private static final List<Pattern> PERSONAL_INFORMATION_PATTERNS = List.of(
            Pattern.compile("\\b01[0-9]-?\\d{3,4}-?\\d{4}\\b"),
            Pattern.compile("\\b[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}\\b"),
            Pattern.compile("\\b\\d{2,4}-\\d{2,4}-\\d{4,6}\\b"),
            Pattern.compile("\\b\\d{10,14}\\b")
    );

    /**
     * 현재 과제 단계에서는 추가적인 의존성을 사용하는데에 제한이 있기에 입력값 공격을 막기 위한 라이브러리까지 도입하지 않고
     * 명백히 위험한 HTML/XSS 패턴과 기초적인 개인정보 패턴만 가볍게 차단하는 처리를 했습니다.
     * 추후 validation annotation 체계로 전환하면 이 검증 로직을 재사용하고 운영 단계에서는 jsoup 같은 sanitizer로
     * 허용 태그/속성 정책을 더 정교하게 관리하는 방향을 고려하고있습니다.
     * 개인정보 탐지도 현재는 하드코딩된 정규식 최소 목록으로 유지하지만 추후에는 설정 파일/전문 탐지 라이브러리로 전환,
     * 분리해 정책 정밀도와 갱신 편의성을 높이는 방향을 고려합니다.
     * 현재는 정책 위반 시 예외로 차단하지 않고 HIDDEN 상태로 전환할지 여부만 반환합니다.
     * 원래는 부적절한 언어에 대한 필터링도 해야 하지만 한국의 욕은 너무 다채로워서 지금처럼 직접 구현을 하게 됐을 때
     * 욕설 패턴 정의만으로 책 한권을 쓰게 될 것 같기에 보류했습니다.
     *
     * @param title 게시글 제목
     * @param content 게시글 본문
     * @return 검수 결과
     */
    public PostModerationResult validate(String title, String content) {
        if (containsDangerousPattern(title) || containsDangerousPattern(content)) {
            return PostModerationResult.hidden("허용되지 않는 HTML 또는 스크립트 패턴이 감지되었습니다.");
        }
        if (containsPersonalInformation(title) || containsPersonalInformation(content)) {
            return PostModerationResult.hidden("개인정보로 추정되는 표현이 감지되었습니다.");
        }
        return PostModerationResult.published();
    }

    /**
     * 위험한 HTML/XSS 패턴 포함 여부를 확인한다.
     *
     * @param value 검사 대상 문자열
     * @return 위험 패턴 포함 여부
     */
    private boolean containsDangerousPattern(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        return DANGEROUS_PATTERNS.stream()
                .anyMatch(normalized::contains);
    }

    /**
     * 단순 개인정보 패턴 포함 여부를 확인한다.
     *
     * @param value 검사 대상 문자열
     * @return 개인정보 추정 패턴 포함 여부
     */
    private boolean containsPersonalInformation(String value) {
        return PERSONAL_INFORMATION_PATTERNS.stream()
                .anyMatch(pattern -> pattern.matcher(value).find());
    }
}
