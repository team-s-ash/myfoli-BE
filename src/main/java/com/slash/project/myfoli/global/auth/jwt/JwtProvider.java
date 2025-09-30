package com.slash.project.myfoli.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.slash.project.myfoli.domain.auth.jwt.entity.RefreshToken;
import com.slash.project.myfoli.domain.auth.jwt.repository.RefreshTokenRepository;
import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import com.slash.project.myfoli.global.auth.jwt.exception.TokenValidationResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final Long ACCESS_EXPIRATION_TIME = 1000L * 60 * 60;
    private static final Long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final String accessHeader = "Authorization";
    private final String refreshHeader = "Authorization-refresh";
    private static final String BEARER = "Bearer ";

    private static final String USERID_CLAIM = "userId";
    private static final String EMAIL_CLAIM = "email";

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String createAccessToken(Long userId, String email) { // AccessToken 발급
        Date now = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + ACCESS_EXPIRATION_TIME))
                .withClaim(USERID_CLAIM, userId)
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() { // RefreshToken 발급
        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secretKey));
    }



    public void setAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, BEARER + accessToken);
    }

    public void setRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);   // JS 접근 불가 → XSS 방지
        cookie.setSecure(false);     // HTTPS에서만 전송 (개발환경에서 http라면 false로)
        cookie.setPath("/");        // 경로 제한 ("/"면 모든 요청에 포함)
        cookie.setMaxAge((int) (REFRESH_EXPIRATION_TIME / 1000));

        response.addCookie(cookie);
    }

    // 각각 AccessToken, RefreshToken, Email 정보를 추출하여 확인용으로 사용


    public Optional<String> extractAccessToken(String authorizationHeader) {
        return Optional.ofNullable(authorizationHeader)
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }


    public Optional<String> extractRefreshToken(String refreshHeader) {
        return Optional.ofNullable(refreshHeader)
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String accessToken) {
        log.info("Access Token : {}", accessToken);
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("EMAIL, 엑세스 토큰이 유효하지 않습니다.{}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> extractUserId(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(USERID_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("USERID, 엑세스 토큰이 유효하지 않습니다.{}", e.getMessage());
            return Optional.empty();
        }
    }

    public void saveRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        refreshTokenRepository.revokeAllByUserEmail(email);

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(REFRESH_EXPIRATION_TIME / 1000))
                .build();
        refreshTokenRepository.save(token);
    }

    public void logout(String email) {
        refreshTokenRepository.revokeAllByUserEmail(email);
    }


    // 지피티식 업그레이드 -->
    public TokenValidationResult validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return TokenValidationResult.VALID;
        } catch (TokenExpiredException e) {
            return TokenValidationResult.EXPIRED;
        } catch (SignatureVerificationException e) {
            return TokenValidationResult.INVALID_SIGNATURE;
        } catch (Exception e) {
            return TokenValidationResult.MALFORMED;
        }
    }

}
