package org.sopt.domain.post.controller;


import org.sopt.domain.post.dto.request.CreatePostRequest;
import org.sopt.domain.post.dto.response.CreatePostResponse;
import org.sopt.domain.post.dto.response.PostResponse;
import org.sopt.domain.post.service.PostService;

import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    // POST /posts
    public CreatePostResponse createPost(CreatePostRequest request) {
        try {
            return postService.createPost(request);
        } catch (IllegalArgumentException e) {
            return new CreatePostResponse(null, "🚫 " + e.getMessage());
        }
    }

    // GET /posts 📝 과제
    public List<PostResponse> getAllPosts() {
        // TODO: postService.getAllPosts() 호출해서 반환
        return null;
    }

    // GET /posts/{id} 📝 과제
    public PostResponse getPost(Long id) {
        // TODO: postService.getPost(id) 호출, 예외 발생 시 null 반환
        return null;
    }

    // PUT /posts/{id} 📝 과제
    public void updatePost(Long id, String newTitle, String newContent) {
        // TODO: postService.updatePost() 호출, 예외 발생 시 에러 메시지 출력
    }

    // DELETE /posts/{id} 📝 과제
    public void deletePost(Long id) {
        // TODO: postService.deletePost() 호출, 예외 발생 시 에러 메시지 출력
    }
}
