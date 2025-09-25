package com.slash.project.myfoli.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.slash.project.myfoli.domain.auth.jwt.entity.RefreshToken;
import com.slash.project.myfoli.domain.auth.jwt.repository.RefreshTokenRepository;
import com.slash.project.myfoli.domain.auth.service.UserAuthService;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import com.slash.project.myfoli.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class JwtProivder {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final Long ACCESS_EXPIRATION_TIME = 1000L * 60 * 60;
    private static final Long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final String accessHeader = "Authorization";
    private final String refreshHeader = "Authorization-refresh";
    private static final String BEARER = "Bearer ";
    private static final String CLAIM = "user_id";

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAuthService userAuthService;
    private final UserRepository userRepository;

    public String AccessTokenCreate(Long user_id) { // AccessToken 발급
        Date now = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + ACCESS_EXPIRATION_TIME))
                .withClaim(CLAIM, user_id)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String RefreshToken() { // RefreshToken 발급
        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secretKey));
    }


   // Access Token 재발급
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("Reissued Access Token : {}", accessToken);
    }

    // 로그인시 2가지 토큰 둘 다 전송
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken,String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessToken(response,accessToken);
        setRefreshToken(response,refreshToken);
        log.info("Reissued Refresh Token : {}", refreshToken);
    }

    public void setAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    // 각각 AccessToken, RefreshToken, Email 정보를 추출하여 확인용으로 사용
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER,""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER,""));
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(CLAIN)
                    .asString());
        } catch (Exception e) {
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public void saveRefreshToken(String email, String refreshToken) {
        Optional<User> user = userRepository.findByEmail(email);

        refreshTokenRepository.revokeAllByUserEmail(email);

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(14))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void logout(String email) {
        refreshTokenRepository.revokeAllByUserEmail(email);
    }

    /*
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.,
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

     */

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}
