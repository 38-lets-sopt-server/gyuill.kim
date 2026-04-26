package org.sopt.domain.post.presentation.controller;

import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.service.PostCommandService;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.PostReactionRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostReactionToggleResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.presentation.mapper.PostResponseMapper;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostCommandController {

    private final PostCommandService postCommandService;
    private final PostResponseMapper postResponseMapper;

    public PostCommandController(PostCommandService postCommandService, PostResponseMapper postResponseMapper) {
        this.postCommandService = postCommandService;
        this.postResponseMapper = postResponseMapper;
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<PostResponse>> createPost(@RequestBody CreatePostRequest request) {
        request.validate();
        CreatePostCommand command = new CreatePostCommand(
                request.boardType(),
                request.title(),
                request.content(),
                request.authorUserId()
        );
        PostResult result = postCommandService.createPost(command);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.success(PostSuccessCode.POST_CREATED, response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CommonApiResponse<Void>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        request.validate();
        UpdatePostCommand command = new UpdatePostCommand(request.title(), request.content());

        postCommandService.updatePost(postId, command);
        return CommonApiResponse.success(PostSuccessCode.POST_UPDATED, null);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonApiResponse<Void>> deletePost(@PathVariable Long postId) {
        postCommandService.deletePost(postId);
        return CommonApiResponse.success(PostSuccessCode.POST_DELETED, null);
    }

    @PostMapping("/{postId}/like/toggle")
    public ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleLikePost(
            @PathVariable Long postId,
            @RequestBody PostReactionRequest request
    ) {
        request.validate();
        boolean reacted = postCommandService.toggleLikePost(postId, request.userId());
        PostReactionToggleResponse response = new PostReactionToggleResponse(reacted);

        return CommonApiResponse.success(PostSuccessCode.POST_LIKE_TOGGLED, response);
    }

    @PostMapping("/{postId}/scrap/toggle")
    public ResponseEntity<CommonApiResponse<PostReactionToggleResponse>> toggleScrapPost(
            @PathVariable Long postId,
            @RequestBody PostReactionRequest request
    ) {
        request.validate();
        boolean reacted = postCommandService.toggleScrapPost(postId, request.userId());
        PostReactionToggleResponse response = new PostReactionToggleResponse(reacted);

        return CommonApiResponse.success(PostSuccessCode.POST_SCRAP_TOGGLED, response);
    }
}
