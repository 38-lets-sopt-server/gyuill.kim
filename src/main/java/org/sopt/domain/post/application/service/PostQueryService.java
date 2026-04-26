package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostPageResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;

    public PostQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostCursorResult getPosts(BoardType boardType, Long cursor, int size) {
        Slice<Post> posts = postRepository.findAllByCursor(boardType, cursor, size);
        return toPostCursorResult(posts);
    }

    public PostPageResult searchPosts(String titleKeyword, String authorNickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.search(titleKeyword, authorNickname, pageable);
        return toPostPageResult(posts);
    }

    private PostCursorResult toPostCursorResult(Slice<Post> posts) {
        List<PostResult> content = posts.getContent().stream()
                .map(post -> new PostResult(
                        post.getId(),
                        post.getBoardType(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthorUser().getNickname(),
                        post.getLikeCount(),
                        post.getCreatedAt()
                ))
                .toList();

        Long nextCursor = posts.hasNext() && !content.isEmpty()
                ? content.get(content.size() - 1).id()
                : null;
        return new PostCursorResult(content, nextCursor, posts.getSize(), posts.hasNext());
    }

    private PostPageResult toPostPageResult(Page<Post> posts) {
        List<PostResult> content = posts.getContent().stream()
                .map(post -> new PostResult(
                        post.getId(),
                        post.getBoardType(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthorUser().getNickname(),
                        post.getLikeCount(),
                        post.getCreatedAt()
                ))
                .toList();

        return new PostPageResult(
                content,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.hasNext()
        );
    }

    public PostResult getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return new PostResult(
                post.getId(),
                post.getBoardType(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorUser().getNickname(),
                post.getLikeCount(),
                post.getCreatedAt()
        );
    }
}
