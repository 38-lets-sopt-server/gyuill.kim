package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.domain.Post;

import java.time.LocalDateTime;

public class PostResponse {
    Long id;
    String title;
    String content;
    String author;
    LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " - " + author + " (" + createdAt + ")\n" + content;
    }
}
