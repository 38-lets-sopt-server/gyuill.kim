package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Deprecated
@Repository
@Profile("in-memory")
public class InMemoryPostRepository implements PostRepository {
    private final List<Post> postList = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    @Override
    public synchronized Post save(Post post) {
        if (post.getId() == null) {
            try {
                var idField = Post.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(post, nextId.getAndIncrement());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to assign id to in-memory post.", e);
            }
            postList.add(post);
            return post;
        }

        postList.removeIf(savedPost -> savedPost.getId().equals(post.getId()));
        postList.add(post);
        return post;
    }

    @Override
    public synchronized Page<Post> findAll(Pageable pageable) {
        return slice(pageable, postList);
    }

    @Override
    public synchronized Page<Post> findAllByBoardType(BoardType boardType, Pageable pageable) {
        List<Post> filteredPosts = postList.stream()
                .filter(post -> post.getBoardType() == boardType)
                .toList();
        return slice(pageable, filteredPosts);
    }

    @Override
    public synchronized Optional<Post> findById(Long id) {
        return postList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public synchronized boolean existsByAuthorUserId(Long authorUserId) {
        return postList.stream()
                .anyMatch(post -> post.getAuthorUser().getId().equals(authorUserId));
    }

    @Override
    public synchronized void deleteById(Long id) {
        postList.removeIf(p -> p.getId().equals(id));
    }

    private Page<Post> slice(Pageable pageable, List<Post> posts) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), posts.size());
        if (start > posts.size()) {
            return new PageImpl<>(List.of(), pageable, posts.size());
        }
        return new PageImpl<>(posts.subList(start, end), pageable, posts.size());
    }
}
