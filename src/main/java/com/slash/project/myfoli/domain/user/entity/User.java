package com.slash.project.myfoli.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Table(name = "user_tbl")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private String email;

    private String role;

    private LocalDateTime createdAt;

    @Builder
    public User(String username, String password, String email, PasswordEncoder passwordEncoder) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @PrePersist
    public void prePersist() {
        this.role = this.role == null ? "USER" : this.role;
        this.createdAt = LocalDateTime.now();
    }

    public void updateUsername(String username) {
        this.username = username;
    }
}
