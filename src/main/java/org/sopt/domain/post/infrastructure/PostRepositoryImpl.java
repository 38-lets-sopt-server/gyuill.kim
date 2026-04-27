package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    public PostRepositoryImpl(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size) {
        return postJpaRepository.findAllByCursor(boardType, cursor, size);
    }

    @Override
    public Slice<Post> search(String keyword, Long cursor, int size) {
        return postJpaRepository.search(keyword, cursor, size);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }
}
