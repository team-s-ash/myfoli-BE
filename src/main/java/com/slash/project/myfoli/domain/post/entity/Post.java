package com.slash.project.myfoli.domain.post.entity;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    private String title;

    private String content;

    private int view_count;

    private String created_at;

    private String updated_at;

    @Enumerated(EnumType.STRING)
    private Status status;
}
