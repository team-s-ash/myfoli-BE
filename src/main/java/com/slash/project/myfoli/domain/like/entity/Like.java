package com.slash.project.myfoli.domain.like.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "like_tbl")
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    //MtM? MtO?
    private Long user_id;

    private Long post_id;

    private String created_at;
}
