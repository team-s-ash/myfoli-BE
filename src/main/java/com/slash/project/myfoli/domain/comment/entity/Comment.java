package com.slash.project.myfoli.domain.comment.entity;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false, length = 500)
    private String content;

    public void updateContent(String content) {
        this.content = content;
    }
}
