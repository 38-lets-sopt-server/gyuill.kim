package org.sopt.domain.post.application.service;

import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // CREATE
    public PostResponse createPost(CreatePostRequest request) {
        request.validate();

        Post post = new Post(
                postRepository.generateId(),
                request.title(),
                request.content(),
                request.author()
        );
        postRepository.save(post);
        return new PostResponse(post);
    }

    // READ - 전체 📝 과제
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .toList();
    }

    // READ - 단건 📝 과제
    public PostResponse getPost(Long id) {
        Post post = findPostOrThrow(id);
        return new PostResponse(post);
    }

    // UPDATE 📝 과제
    public void updatePost(Long id, UpdatePostRequest request) {
        request.validate();

        Post post = findPostOrThrow(id);
        post.update(request.title(), request.content());
    }

    // DELETE 📝 과제
    public void deletePost(Long id) {
        findPostOrThrow(id);
        postRepository.deleteById(id);
    }

    private Post findPostOrThrow(Long id) {
        Post post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return post;
    }
}
