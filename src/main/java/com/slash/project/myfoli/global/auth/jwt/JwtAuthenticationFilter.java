package com.slash.project.myfoli.global.auth.jwt;

import com.slash.project.myfoli.domain.user.entity.User;
import com.slash.project.myfoli.domain.user.repository.UserRepository;
import com.slash.project.myfoli.global.auth.jwt.exception.TokenValidationResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtProvider.extractAccessToken(request.getHeader("Authorization"))
                .ifPresent(this::checkAccessTokenAndAuthentication);

        filterChain.doFilter(request, response);
    }

    private void checkAccessTokenAndAuthentication(String accessToken) {
        log.debug("Validating access token: {}", accessToken);
        if (jwtProvider.validateToken(accessToken) == TokenValidationResult.VALID) {
            jwtProvider.extractEmail(accessToken)
                    .flatMap(userRepository::findByEmail)
                    .ifPresent(this::saveAuthentication);
        }
    }

    private void saveAuthentication(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // password는 실제 인증에 사용되지 않으므로 DB 값 그대로 전달
                .roles("USER") // 역할(Role) 설정. 필요에 따라 DB에서 가져오도록 수정 가능
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // credentials는 사용하지 않으므로 null
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication successful for user: {}", user.getEmail());
    }
}
