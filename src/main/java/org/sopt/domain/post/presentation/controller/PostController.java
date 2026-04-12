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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts
    public ApiResponse<PostResponse> createPost(CreatePostRequest request) {
        request.validate();
        PostResult result = postService.createPost(
                CreatePostCommand.of(request.title(), request.content(), request.author())
        );
        return ApiResponse.success(PostSuccessCode.POST_CREATED, PostResponse.from(result));
    }

    // GET /posts
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts().stream()
                .map(PostResponse::from)
                .toList();
        return ApiResponse.success(PostSuccessCode.POST_LIST_READ, responses);
    }

    // GET /posts/{id}
    public ApiResponse<PostResponse> getPost(Long id) {
        PostResult result = postService.getPost(id);
        return ApiResponse.success(PostSuccessCode.POST_READ, PostResponse.from(result));
    }

    // PUT /posts/{id}
    public ApiResponse<Void> updatePost(Long id, UpdatePostRequest request) {
        request.validate();
        postService.updatePost(id, UpdatePostCommand.of(request.title(), request.content()));
        return ApiResponse.success(PostSuccessCode.POST_UPDATED, null);
    }

    // DELETE /posts/{id}
    public ApiResponse<Void> deletePost(Long id) {
        postService.deletePost(id);
        return ApiResponse.success(PostSuccessCode.POST_DELETED, null);
    }
}
