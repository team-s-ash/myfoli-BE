package com.slash.project.myfoli.domain.post.dto;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.post.entity.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PortfolioResponse {
    private Long postId;
    private String title;
    private String content;
    private int viewCount;
    private Status status;
    private List<String> imageUrls;
    private String authorName;
    private Long authorId;
    private long commentCount;

    public PortfolioResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.status = post.getStatus();
        this.imageUrls = post.getImageUrls();
        this.authorName = post.getUser().getUsername();
        this.authorId = post.getUser().getUserId();
        this.commentCount = 0; // 기본값, 서비스에서 설정
    }
}
