package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.domain.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public PostResponse(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " - " + author + " (" + createdAt + ")\n" + content;
    }
}
