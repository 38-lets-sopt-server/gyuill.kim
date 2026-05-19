package org.sopt.domain.post.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.presentation.dto.request.GetPostsRequest;
import org.sopt.domain.post.presentation.dto.request.SearchPostsRequest;
import org.sopt.domain.post.presentation.dto.response.PostCursorPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Post", description = "게시글 조회 API")
public interface PostQueryControllerDocs {

    @Operation(summary = "게시글 목록 조회", description = "게시판 타입과 커서 기반 페이징으로 게시글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostCursorPageResponse>> getAllPosts(
            @Valid @ModelAttribute GetPostsRequest request
    );

    @Operation(summary = "게시글 검색", description = "키워드와 커서 기반 페이징으로 게시글을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 검색 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostCursorPageResponse>> searchPosts(
            @Valid @ModelAttribute SearchPostsRequest request
    );

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostResponse>> getPost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId
    );

    @Operation(summary = "숨김 게시글 조회", description = "일반 공개 상세 조회와 달리 숨김 상태 게시글 확인에 사용합니다.")
    @ApiResponse(responseCode = "200", description = "숨김 게시글 조회 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<PostResponse>> getHiddenPost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId
    );
}
