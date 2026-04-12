package org.sopt.domain.post.controller;

import java.util.List;

import org.sopt.domain.post.code.PostSuccessCode;
import org.sopt.domain.post.dto.request.CreatePostRequest;
import org.sopt.domain.post.dto.request.UpdatePostRequest;
import org.sopt.domain.post.dto.response.PostResponse;
import org.sopt.domain.post.service.PostService;
import org.sopt.global.response.ApiResponse;

public class PostController {
    private final PostService postService = new PostService();

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
