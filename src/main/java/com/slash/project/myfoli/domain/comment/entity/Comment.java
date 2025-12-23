package com.slash.project.myfoli.domain.comment.entity;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "comment_tbl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String createdAt;

    @Setter
    private String updatedAt;

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now().toString();
    }

    // Getter methods for backward compatibility
    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }

    public Long getPostId() {
        return post != null ? post.getPostId() : null;
    }
}
