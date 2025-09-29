package com.slash.project.myfoli.domain.auth.presentation.controller;

import com.slash.project.myfoli.domain.auth.presentation.dto.LoginRequest;
import com.slash.project.myfoli.domain.auth.presentation.dto.LoginResponse;
import com.slash.project.myfoli.domain.auth.presentation.dto.SignUpRequest;
import com.slash.project.myfoli.domain.auth.service.UserAuthService;
import com.slash.project.myfoli.domain.user.service.UserService;
import com.slash.project.myfoli.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Logout) throws Exception{

    }
}
