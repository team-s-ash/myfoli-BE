package com.slash.project.myfoli.domain.like.entity;

import com.slash.project.myfoli.domain.post.entity.Post;
import com.slash.project.myfoli.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "like_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Like of(Post post, User user) {
        return Like.builder()
                .post(post)
                .user(user)
                .build();
    }
}
