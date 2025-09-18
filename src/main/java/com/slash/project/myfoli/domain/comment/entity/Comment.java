package com.slash.project.myfoli.domain.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comment_tbl")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;

    private Long user_id;

    private Long post_id;

    private String content;

    private String created_at;

    private String updated_at;
}
