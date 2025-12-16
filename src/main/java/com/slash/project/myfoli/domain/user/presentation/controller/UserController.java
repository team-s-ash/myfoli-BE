package com.slash.project.myfoli.domain.user.presentation.controller;

import com.slash.project.myfoli.domain.user.presentation.dto.MyInfoResponse;
import com.slash.project.myfoli.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/expire")
    public ResponseEntity<?> expire() {
        return ResponseEntity.ok("api  answer successful");
    }

    // 내 정보 조회 API
    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getMyInfo() {
        return ResponseEntity.ok(userService.getMyInfo());
    }
}
