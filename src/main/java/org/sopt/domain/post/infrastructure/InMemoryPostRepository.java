package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPostRepository implements PostRepository {
    private final List<Post> postList = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    @Override
    public synchronized Post save(Post post) {
        post.assignId(nextId.getAndIncrement());
        postList.add(post);
        return post;
    }

    @Override
    public synchronized List<Post> findAll() {
        return List.copyOf(postList);
    }

    @Override
    public synchronized List<Post> findAllByBoardType(BoardType boardType) {
        return postList.stream()
                .filter(post -> post.getBoardType() == boardType)
                .toList();
    }

    @Override
    public synchronized Optional<Post> findById(Long id) {
        return postList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public synchronized void deleteById(Long id) {
        postList.removeIf(p -> p.getId().equals(id));
    }

}
