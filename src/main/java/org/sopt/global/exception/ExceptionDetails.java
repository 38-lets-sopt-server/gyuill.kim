package org.sopt.global.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 예외 응답에 실리는 상세 정보 맵을 일관된 순서로 구성하기 위한 얇은 빌더.
 * 도메인별 팩토리가 LinkedHashMap 보일러플레이트 없이 필드 추가에 집중하도록 돕는다.
 * 원래는 Postdptj 예외 응답을 구체화 하기 위해 구현한 {@PostStateExceptionDetailsFactory.java} 와 통합돼있었지만
 * 도메인 확장시 반복 가능성이 높은 패턴이라고 생각이 들어서 분리했습니다.
 */
public final class ExceptionDetails {

    private ExceptionDetails() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> details = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder put(String key, Object value) {
            details.put(key, value);
            return this;
        }

        public Map<String, Object> build() {
            return Collections.unmodifiableMap(new LinkedHashMap<>(details));
        }
    }
}
