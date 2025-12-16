package com.slash.project.myfoli.domain.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    @GetMapping("/expire")
    public ResponseEntity<?> expire() {
        return ResponseEntity.ok("api  answer successful");
    }
}
