package org.sopt.domain.post.controller;


import org.sopt.domain.post.dto.request.CreatePostRequest;
import org.sopt.domain.post.dto.request.UpdatePostRequest;
import org.sopt.domain.post.dto.response.CreatePostResponse;
import org.sopt.domain.post.dto.response.PostResponse;
import org.sopt.domain.post.service.PostService;
import org.sopt.global.response.ApiResponse;

import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    // POST /posts
    public ApiResponse<CreatePostResponse> createPost(CreatePostRequest request) {
        try {
            CreatePostResponse response = postService.createPost(request);
            return ApiResponse.success(response.message, response);
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // GET /posts 📝 과제
    public ApiResponse<List<PostResponse>> getAllPosts() {
        try {
            return ApiResponse.success("게시글 목록 조회 성공", postService.getAllPosts());
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // GET /posts/{id} 📝 과제
    public ApiResponse<PostResponse> getPost(Long id) {
        try {
            return ApiResponse.success("게시글 조회 성공", postService.getPost(id));
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // PUT /posts/{id} 📝 과제
    public ApiResponse<Void> updatePost(Long id, UpdatePostRequest request) {
        try {
            postService.updatePost(id, request);
            return ApiResponse.success("게시글 수정 완료!", null);
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // DELETE /posts/{id} 📝 과제
    public ApiResponse<Void> deletePost(Long id) {
        try {
            postService.deletePost(id);
            return ApiResponse.success("게시글 삭제 완료!", null);
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
}
