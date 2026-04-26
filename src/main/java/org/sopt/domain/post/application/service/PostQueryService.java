package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.dto.PostPageResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PostPageResult getPosts(BoardType boardType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        Page<Post> posts = boardType == null
                ? postRepository.findAll(pageable)
                : postRepository.findAllByBoardType(boardType, pageable);
        return toPostPageResult(posts);
    }

    public PostPageResult searchPosts(String titleKeyword, String authorNickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.search(titleKeyword, authorNickname, pageable);
        return toPostPageResult(posts);
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
