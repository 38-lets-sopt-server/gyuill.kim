package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.port.UserPort;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.sopt.domain.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostCommandService {

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final UserPort userPort;

    public PostCommandService(PostRepository postRepository, PostReactionRepository postReactionRepository, UserPort userPort) {
        this.postRepository = postRepository;
        this.postReactionRepository = postReactionRepository;
        this.userPort = userPort;
    }

    public PostResult createPost(CreatePostCommand command) {
        User authorUser = userPort.getUser(command.authorUserId());
        Post post = postRepository.save(new Post(
                command.boardType(),
                command.title(),
                command.content(),
                authorUser
        ));
        return new PostResult(
                post.getId(),
                post.getBoardType(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorUser().getNickname(),
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

    public boolean toggleLikePost(Long postId, Long userId) {
        return toggleReaction(postId, userId, ReactionType.LIKE);
    }

    public boolean toggleScrapPost(Long postId, Long userId) {
        return toggleReaction(postId, userId, ReactionType.SCRAP);
    }

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    /**
     * 반응 등록/취소 API를 분리하면 클라이언트가 현재 반응 상태를 정확히 알고 있어야 한다.
     * 과제 범위에서는 인증과 상태 동기화 계층이 아직 없으므로 화면 동작에 맞춰 토글 API로 단순화한다.
     * 또한 현재 반응 모델은 엔티티 수정보다 조인 엔티티의 insert/delete 성격이 강해
     * 버전 락보다는 DB 유니크 제약이 더 직접적인 중복 방어 수단에 가깝다.
     * 이후 인증/인가와 멱등 정책이 구체화되면 등록/취소 분리로 재검토할 수 있다.
     *
     * @return 토글 후 반응이 활성화된 상태면 {@code true}, 해제된 상태면 {@code false}
     */
    private boolean toggleReaction(Long postId, Long userId, ReactionType type) {
        Post post = findPostOrThrow(postId);
        User user = userPort.getUser(userId);
        if (postReactionRepository.existsByPostIdAndUserIdAndType(postId, userId, type)) {
            postReactionRepository.deleteByPostIdAndUserIdAndType(postId, userId, type);
            return false;
        }
        postReactionRepository.save(new PostReaction(post, user, type));
        return true;
    }
}
