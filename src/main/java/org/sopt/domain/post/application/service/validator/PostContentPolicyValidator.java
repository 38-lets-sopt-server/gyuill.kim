package org.sopt.domain.post.application.service.validator;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

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
     * 현재 과제 단계에서는 추가적인 의존성을 사용하는데에 제한이 있기에 입력값 공격을 막기 위한 라이브러리까지 도입하지 않고
     * 명백히 위험한 HTML/XSS 패턴만 가볍게 차단하는 처리를 했습니다.
     * 추후 validation annotation 체계로 전환하면 이 검증 로직을 재사용하고 운영 단계에서는 jsoup 같은 sanitizer로
     * 허용 태그/속성 정책을 더 정교하게 관리하는 방향을 고려하고있습니다.
     * 추가로 스토리보드를 충실히 따른다면 작성을 막는게 아닌 숨김처리를 하는게 맞지만 아직 게시글 상태 로직이 구현되지 않았기 때문에 보류합니다
     */
    public void validate(String title, String content) {
        validateTitle(title);
        validateContent(content);
    }

    public void validateTitle(String title) {
        if (containsDangerousPattern(title)) {
            throw new BaseException(PostErrorCode.UNSAFE_POST_TITLE);
        }
    }

    public void validateContent(String content) {
        if (containsDangerousPattern(content)) {
            throw new BaseException(PostErrorCode.UNSAFE_POST_CONTENT);
        }
    }

    private boolean containsDangerousPattern(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        return DANGEROUS_PATTERNS.stream()
                .anyMatch(normalized::contains);
    }
}
