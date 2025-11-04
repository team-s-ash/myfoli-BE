package com.slash.project.myfoli.domain.auth.presentation.controller;

import com.slash.project.myfoli.domain.auth.presentation.dto.LoginRequest;
import com.slash.project.myfoli.domain.auth.presentation.dto.LoginResponse;
import com.slash.project.myfoli.domain.auth.presentation.dto.SignUpRequest;
import com.slash.project.myfoli.domain.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) throws Exception{
        userAuthService.signUp(signUpRequest);
        return ResponseEntity.ok("signup successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception{
        LoginResponse loginResponse = userAuthService.logIn(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        // 현재 인증된 사용자의 정보를 기반으로 로그아웃 처리
        userAuthService.logout(authentication);
        return ResponseEntity.ok("logout successful");
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@RequestHeader("Authorization-refresh") String refreshToken) {
        LoginResponse loginResponse = userAuthService.reissueToken(refreshToken);
        return ResponseEntity.ok(loginResponse);
    }


}
