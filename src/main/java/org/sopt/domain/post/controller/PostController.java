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
        return postService.createPost(request);
    }

    // GET /posts 📝 과제
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    // GET /posts/{id} 📝 과제
    public PostResponse getPost(Long id) {
        return postService.getPost(id);
    }

    // PUT /posts/{id} 📝 과제
    public void updatePost(Long id, String newTitle, String newContent) {
        postService.updatePost(id, newTitle, newContent);
    }

    // DELETE /posts/{id} 📝 과제
    public void deletePost(Long id) {
        postService.deletePost(id);
    }
}
