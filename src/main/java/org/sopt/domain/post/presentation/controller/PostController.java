package org.sopt.domain.post.presentation.controller;

import java.util.List;

import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.application.service.PostService;
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
        PostResponse response = postService.createPost(request);
        return ApiResponse.success(PostSuccessCode.POST_CREATED, response);
    }

    // GET /posts
    public ApiResponse<List<PostResponse>> getAllPosts() {
        return ApiResponse.success(PostSuccessCode.POST_LIST_READ, postService.getAllPosts());
    }

    // GET /posts/{id}
    public ApiResponse<PostResponse> getPost(Long id) {
        return ApiResponse.success(PostSuccessCode.POST_READ, postService.getPost(id));
    }

    // PUT /posts/{id}
    public ApiResponse<Void> updatePost(Long id, UpdatePostRequest request) {
        postService.updatePost(id, request);
        return ApiResponse.success(PostSuccessCode.POST_UPDATED, null);
    }

    // DELETE /posts/{id}
    public ApiResponse<Void> deletePost(Long id) {
        postService.deletePost(id);
        return ApiResponse.success(PostSuccessCode.POST_DELETED, null);
    }
}
