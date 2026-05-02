package org.sopt.domain.post.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.service.PostCommandService;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.PostReactionRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostReactionToggleResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.presentation.mapper.PostResponseMapper;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시글 쓰기 API를 제공하는 컨트롤러.
 * 생성/수정/삭제와 반응 토글을 같은 command 책임으로 묶는다.
 */
@RestController
@RequestMapping("/posts")
@Tag(name = "Post", description = "게시글 생성, 수정, 삭제 및 반응 처리 API")
public class PostCommandController {

    private final PostCommandService postCommandService;
    private final PostResponseMapper postResponseMapper;

    public PostCommandController(PostCommandService postCommandService, PostResponseMapper postResponseMapper) {
        this.postCommandService = postCommandService;
        this.postResponseMapper = postResponseMapper;
    }

    /**
     * 게시글을 생성한다.
     *
     * @param request 게시글 생성 요청
     * @return 생성된 게시글 응답
     */
    @PostMapping
    @Operation(summary = "게시글 작성", description = "새 게시글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 생성 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ) {
        request.validate();
        CreatePostCommand command = new CreatePostCommand(
                request.boardType(),
                request.title(),
                request.content(),
                request.authorUserId(),
                request.isAnonymous()
        );
        PostResult result = postCommandService.createPost(command);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_CREATED, response);
    }

    /**
     * 게시글 제목과 본문을 수정한다.
     *
     * @param postId 게시글 ID
     * @param request 게시글 수정 요청
     * @return 공통 성공 응답
     */
    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "기존 게시글의 제목과 본문을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<Void>> updatePost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request
    ) {
        request.validate();
        UpdatePostCommand command = new UpdatePostCommand(request.title(), request.content());

        postCommandService.updatePost(postId, command);
        return CommonApiResponse.successResponse(PostSuccessCode.POST_UPDATED, null);
    }

    /**
     * 게시글을 삭제한다.
     *
     * @param postId 게시글 ID
     * @return 공통 성공 응답
     */
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<Void>> deletePost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(postId);
        return CommonApiResponse.successResponse(PostSuccessCode.POST_DELETED, null);
    }

    /**
     * 게시글 좋아요 상태를 토글한다.
     *
     * @param postId 게시글 ID
     * @param request 반응 요청
     * @return 토글 후 반응 상태
     */
    @PostMapping("/{postId}/like/toggle")
    @Operation(summary = "게시글 공감 토글", description = "게시글 공감 상태를 토글합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 공감 토글 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleLikePost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @RequestBody PostReactionRequest request
    ) {
        request.validate();
        boolean reacted = postCommandService.toggleLikePost(postId, request.userId());
        PostReactionToggleResponse response = new PostReactionToggleResponse(reacted);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_LIKE_TOGGLED, response);
    }

    /**
     * 게시글 스크랩 상태를 토글한다.
     *
     * @param postId 게시글 ID
     * @param request 반응 요청
     * @return 토글 후 반응 상태
     */
    @PostMapping("/{postId}/scrap/toggle")
    @Operation(summary = "게시글 스크랩 토글", description = "게시글 스크랩 상태를 토글합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 스크랩 토글 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleScrapPost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId,
            @RequestBody PostReactionRequest request
    ) {
        request.validate();
        boolean reacted = postCommandService.toggleScrapPost(postId, request.userId());
        PostReactionToggleResponse response = new PostReactionToggleResponse(reacted);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_SCRAP_TOGGLED, response);
    }
}
