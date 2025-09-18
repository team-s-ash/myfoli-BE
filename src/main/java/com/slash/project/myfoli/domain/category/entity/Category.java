package com.slash.project.myfoli.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "category_tbl")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    private String name;

    private String description;
}
