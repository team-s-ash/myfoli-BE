package com.slash.project.myfoli.domain.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    //MtM? MtO?
    private Long user_id;

    private Long post_id;

    private String created_at;
}
