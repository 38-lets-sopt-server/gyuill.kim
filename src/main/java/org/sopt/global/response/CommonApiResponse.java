package org.sopt.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Schema(description = "공통 API 응답 래퍼")
/**
 * 모든 API가 공통 형식으로 응답하도록 감싸는 래퍼.
 * 과제 설명과 테스트에서 일관된 응답 구조를 보여주기 위해 도메인별 응답도 동일 포맷을 사용한다.
 */
public record CommonApiResponse<T>(
        @Schema(description = "응답 코드", example = "POST-201")
        String code,
        @Schema(description = "성공 여부", example = "true")
        boolean success,
        @Schema(description = "응답 메시지", example = "게시글 생성 성공")
        String message,
        @Schema(description = "응답 데이터")
        T data,
        @Schema(description = "실패 시 부가 상세 정보", example = "{\"postId\":1}", nullable = true)
        Map<String, Object> details
) {

    /**
     * 성공 코드를 기준으로 최종 HTTP 응답을 생성한다.
     *
     * @param successCode 성공 코드
     * @param data 응답 데이터
     * @return ResponseEntity 기반 성공 응답
     * @param <T> 데이터 타입
     */
    public static <T> ResponseEntity<CommonApiResponse<T>> successResponse(SuccessCode successCode, T data) {
        if (successCode.getHttpStatus() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity
                .status(successCode.getHttpStatus())
                .body(successBody(successCode, data));
    }

    /**
     * 성공 본문만 생성한다.
     *
     * @param successCode 성공 코드
     * @param data 응답 데이터
     * @return 공통 성공 응답 본문
     * @param <T> 데이터 타입
     */
    public static <T> CommonApiResponse<T> successBody(SuccessCode successCode, T data) {
        return new CommonApiResponse<>(successCode.getCode(), true, successCode.getMessage(), data, null);
    }

    /**
     * 상세 정보 없는 실패 응답 본문을 생성한다.
     *
     * @param errorCode 에러 코드
     * @return 공통 실패 응답 본문
     * @param <T> 데이터 타입
     */
    public static <T> CommonApiResponse<T> failureBody(ErrorCode errorCode) {
        return new CommonApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null, null);
    }

    /**
     * 구조화 상세 정보를 포함한 실패 응답 본문을 생성한다.
     *
     * @param errorCode 에러 코드
     * @param details 실패 상세 정보
     * @return 공통 실패 응답 본문
     * @param <T> 데이터 타입
     */
    public static <T> CommonApiResponse<T> failureBody(ErrorCode errorCode, Map<String, Object> details) {
        return new CommonApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null, details);
    }
}
