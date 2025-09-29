package com.slash.project.myfoli.domain.auth.jwt.entity;

import com.slash.project.myfoli.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "refreshtoken_tbl")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    private String token; // 실제 JWT Refresh Token 값
    private LocalDateTime expiresAt; // 만료 시간

    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    private boolean revoked = false; // 강제 무효화 여부 (로그아웃 등)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user; // 소유 사용자 (단방향만 두는 걸 추천)

    @Builder
    public RefreshToken(User user, String token, LocalDateTime expiresAt) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // === 비즈니스 로직 ===
    /** 만료 여부 확인 */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    /** 유효 여부 확인 */
    public boolean isValid() {
        return !this.revoked && !isExpired();
    }

    /** 강제 무효화 */
    public void revoke() {
        this.revoked = true;
    }
}
