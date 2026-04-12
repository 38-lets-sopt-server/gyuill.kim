package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryPostRepository implements PostRepository {
    private final List<Post> postList = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public void save(Post post) {
        postList.add(post);
    }

    @Override
    public List<Post> findAll() {
        return postList;
    }

    @Override
    public Post findById(Long id) {
        return postList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        postList.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public Long generateId() {
        return nextId++;
    }
}
