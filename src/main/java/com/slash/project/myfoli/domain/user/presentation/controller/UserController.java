package com.slash.project.myfoli.domain.user.presentation.controller;

import com.slash.project.myfoli.domain.user.presentation.dto.MyInfoResponse;
import com.slash.project.myfoli.domain.user.presentation.dto.UpdateMyInfoRequest;
import com.slash.project.myfoli.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MyInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUsername()));
    }

    // 내 정보 수정 API
    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateMyInfoRequest dto) {
        userService.updateMyInfo(userDetails.getUsername(), dto);
        return ResponseEntity.noContent().build();
    }
}
