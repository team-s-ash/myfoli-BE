package com.slash.project.myfoli.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Table(name = "user_tbl")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String username;

    private String password;

    private String email;

    private String role;

    private LocalDateTime created_at;

    @Builder
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @PrePersist
    public void prePersist() {
        this.role = this.role == null ? "USER" : this.role;
        this.created_at = LocalDateTime.now();
    }
}
