package org.sopt.domain.post.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostReactionToggleResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.sopt.global.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 생성, 수정, 삭제 및 반응 처리 API")
public interface PostCommandControllerDocs {

    @Operation(summary = "게시글 작성", description = "새 게시글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 생성 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );

    @Operation(summary = "게시글 수정", description = "기존 게시글의 제목과 본문을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<Void>> updatePost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<Void>> deletePost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );

    @Operation(summary = "게시글 공감 토글", description = "게시글 공감 상태를 토글합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 공감 토글 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleLikePost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );

    @Operation(summary = "게시글 스크랩 토글", description = "게시글 스크랩 상태를 토글합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 스크랩 토글 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleScrapPost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );
}
