package org.sopt.domain.post.domain.model;

import java.time.LocalDateTime;

public class Post {
    private final Long id;
    private String title;
    private String content;
    private final String author;
    private final LocalDateTime createdAt;

    public Post(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
