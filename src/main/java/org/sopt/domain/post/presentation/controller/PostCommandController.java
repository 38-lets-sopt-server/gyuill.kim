package org.sopt.domain.post.presentation.controller;

import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.service.PostCommandService;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.PostReactionRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
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

    public PostCommandController(PostCommandService postCommandService) {
        this.postCommandService = postCommandService;
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<PostResponse>> createPost(@RequestBody CreatePostRequest request) {
        request.validate();
        PostResult result = postCommandService.createPost(new CreatePostCommand(
                request.boardType(),
                request.title(),
                request.content(),
                request.authorUserId()
        ));
        return CommonApiResponse.success(PostSuccessCode.POST_CREATED, new PostResponse(
                result.id(),
                result.boardType(),
                result.title(),
                result.content(),
                result.author(),
                result.createdAt()
        ));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CommonApiResponse<Void>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        request.validate();
        postCommandService.updatePost(postId, new UpdatePostCommand(request.title(), request.content()));
        return CommonApiResponse.success(PostSuccessCode.POST_UPDATED, null);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonApiResponse<Void>> deletePost(@PathVariable Long postId) {
        postCommandService.deletePost(postId);
        return CommonApiResponse.success(PostSuccessCode.POST_DELETED, null);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<CommonApiResponse<Void>> likePost(@PathVariable Long postId, @RequestBody PostReactionRequest request) {
        request.validate();
        postCommandService.likePost(postId, request.userId());
        return CommonApiResponse.success(PostSuccessCode.POST_LIKE_CREATED, null);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<CommonApiResponse<Void>> cancelLikePost(@PathVariable Long postId, @RequestBody PostReactionRequest request) {
        request.validate();
        postCommandService.cancelLikePost(postId, request.userId());
        return CommonApiResponse.success(PostSuccessCode.POST_LIKE_DELETED, null);
    }

    @PostMapping("/{postId}/scrap")
    public ResponseEntity<CommonApiResponse<Void>> scrapPost(@PathVariable Long postId, @RequestBody PostReactionRequest request) {
        request.validate();
        postCommandService.scrapPost(postId, request.userId());
        return CommonApiResponse.success(PostSuccessCode.POST_SCRAP_CREATED, null);
    }

    @DeleteMapping("/{postId}/scrap")
    public ResponseEntity<CommonApiResponse<Void>> cancelScrapPost(@PathVariable Long postId, @RequestBody PostReactionRequest request) {
        request.validate();
        postCommandService.cancelScrapPost(postId, request.userId());
        return CommonApiResponse.success(PostSuccessCode.POST_SCRAP_DELETED, null);
    }
}
