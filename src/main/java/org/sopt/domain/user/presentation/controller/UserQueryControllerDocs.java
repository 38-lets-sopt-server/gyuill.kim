package org.sopt.domain.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "User", description = "사용자 조회 API")
public interface UserQueryControllerDocs {

    @Operation(summary = "사용자 목록 조회", description = "전체 사용자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<List<UserResponse>>> getUsers();

    @Operation(summary = "사용자 상세 조회", description = "사용자 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 상세 조회 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<UserResponse>> getUser(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    );
}
