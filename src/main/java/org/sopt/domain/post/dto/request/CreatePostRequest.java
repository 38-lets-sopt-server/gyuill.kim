package org.sopt.domain.post.dto.request;

public class CreatePostRequest {
    public String title;
    public String content;
    public String author;

    public CreatePostRequest(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
