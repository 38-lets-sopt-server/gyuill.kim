package org.sopt.domain.post.presentation.controller;

import java.util.List;

import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.service.PostService;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.global.response.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody CreatePostRequest request) {
        request.validate();
        PostResult result = postService.createPost(
                CreatePostCommand.of(request.title(), request.content(), request.author())
        );
        return ApiResponse.success(PostSuccessCode.POST_CREATED, PostResponse.from(result));
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts().stream()
                .map(PostResponse::from)
                .toList();
        return ApiResponse.success(PostSuccessCode.POST_LIST_READ, responses);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResult result = postService.getPost(postId);
        return ApiResponse.success(PostSuccessCode.POST_READ, PostResponse.from(result));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<Void> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        request.validate();
        postService.updatePost(postId, UpdatePostCommand.of(request.title(), request.content()));
        return ApiResponse.success(PostSuccessCode.POST_UPDATED, null);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.success(PostSuccessCode.POST_DELETED, null);
    }
}
