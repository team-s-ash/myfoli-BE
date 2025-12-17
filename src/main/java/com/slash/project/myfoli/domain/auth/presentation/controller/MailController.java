package com.slash.project.myfoli.domain.auth.presentation.controller;

import com.slash.project.myfoli.domain.auth.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mail")
public class MailController {
    private final MailService mailService;
    

    // 인증 이메일 전송
    @PostMapping("/mailSend")
    public ResponseEntity<HashMap<String, Object>> mailSend(@RequestParam String mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            mailService.sendMail(mail);
            map.put("success", Boolean.TRUE);
            map.put("message", "Verification email sent successfully.");
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(map);
        }

        return ResponseEntity.ok(map);
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String mail, @RequestParam String userNumber) {

        boolean isMatch = mailService.verifyMailCode(mail, userNumber);

        return ResponseEntity.ok(isMatch);
    }
}
