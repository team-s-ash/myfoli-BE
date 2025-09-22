package com.slash.project.myfoli.domain.auth.presentation.controller;

import com.slash.project.myfoli.domain.auth.presentation.dto.SignUpRequest;
import com.slash.project.myfoli.domain.auth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequest signUpRequest) throws Exception{
        userAuthService.signUp(signUpRequest);
        return "signup seccuss";
    }

}
