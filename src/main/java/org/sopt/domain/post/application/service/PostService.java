package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostPageResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
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

    public PostResult createPost(CreatePostCommand command) {
        Post post = new Post(
                command.boardType(),
                command.title(),
                command.content(),
                command.author()
        );
        Post savedPost = postRepository.save(post);
        return new PostResult(
                savedPost.getId(),
                savedPost.getBoardType(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getAuthor(),
                savedPost.getCreatedAt()
        );
    }

    public PostPageResult getPosts(BoardType boardType, int page, int size) {
        List<Post> posts = boardType == null
                ? postRepository.findAll()
                : postRepository.findAllByBoardType(boardType);
        int totalElements = posts.size();
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<PostResult> content = posts.subList(fromIndex, toIndex).stream()
                .map(post -> new PostResult(
                        post.getId(),
                        post.getBoardType(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor(),
                        post.getCreatedAt()
                ))
                .toList();

        return new PostPageResult(
                content,
                page,
                size,
                totalElements,
                totalPages,
                page + 1 < totalPages
        );
    }

    public PostResult getPost(Long id) {
        Post post = findPostOrThrow(id);
        return new PostResult(
                post.getId(),
                post.getBoardType(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }

    public void updatePost(Long id, UpdatePostCommand command) {
        Post post = findPostOrThrow(id);
        post.update(command.title(), command.content());
    }

    public void deletePost(Long id) {
        findPostOrThrow(id);
        postRepository.deleteById(id);
    }

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }
}
