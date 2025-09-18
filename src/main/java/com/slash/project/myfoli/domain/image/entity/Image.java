package com.slash.project.myfoli.domain.image.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "image_tbl")
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long image_id;

    private Long post_id;

    private String file_name;

    private String file_path;

    private String uploaded_at;
}
