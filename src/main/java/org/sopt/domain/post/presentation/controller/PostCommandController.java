package org.sopt.domain.post.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.service.PostCommandService;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostReactionToggleResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.presentation.mapper.PostResponseMapper;
import org.sopt.global.response.CommonApiResponse;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequiredArgsConstructor
public class PostCommandController implements PostCommandControllerDocs {

    private final PostCommandService postCommandService;
    private final PostResponseMapper postResponseMapper;

    /**
     * 게시글을 생성한다.
     *
     * @param request 게시글 생성 요청
     * @return 생성된 게시글 응답
     */
    @PostMapping
    @Override
    public ResponseEntity<CommonApiResponse<PostResponse>> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        CreatePostCommand command = new CreatePostCommand(
                request.boardType(),
                request.title(),
                request.content(),
                authenticatedUser.userId(),
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
    @Override
    public ResponseEntity<CommonApiResponse<Void>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        UpdatePostCommand command = new UpdatePostCommand(request.title(), request.content());

        postCommandService.updatePost(postId, authenticatedUser.userId(), command);
        return CommonApiResponse.successResponse(PostSuccessCode.POST_UPDATED, null);
    }

    /**
     * 게시글을 삭제한다.
     *
     * @param postId 게시글 ID
     * @return 공통 성공 응답
     */
    @DeleteMapping("/{postId}")
    @Override
    public ResponseEntity<CommonApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        postCommandService.deletePost(postId, authenticatedUser.userId());
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
    @Override
    public ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleLikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        boolean reacted = postCommandService.toggleLikePost(postId, authenticatedUser.userId());
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
    @Override
    public ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleScrapPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        boolean reacted = postCommandService.toggleScrapPost(postId, authenticatedUser.userId());
        PostReactionToggleResponse response = new PostReactionToggleResponse(reacted);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_SCRAP_TOGGLED, response);
    }
}
