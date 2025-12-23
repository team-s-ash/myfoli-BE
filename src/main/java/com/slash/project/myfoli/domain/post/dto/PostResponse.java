package com.slash.project.myfoli.domain.post.dto;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.entity.Status;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private int viewCount;
    private Status status;
    private List<String> imageUrls;
    private String authorName;
    private Long authorId;

    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.status = post.getStatus();
        this.imageUrls = post.getImageUrls();
        this.authorName = post.getUser().getUsername();
        this.authorId = post.getUser().getUserId();
    }
}
