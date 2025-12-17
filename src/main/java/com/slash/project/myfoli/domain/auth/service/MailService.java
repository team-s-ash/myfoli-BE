package com.slash.project.myfoli.domain.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "MyfoliMailService@gmail.com";
    private final Map<String, String> verificationCodes = new HashMap<>();

    private String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * (90000)) + 100000);
    }

    public void sendMail(String mail) {
        String verificationCode = generateVerificationCode();
        verificationCodes.put(mail, verificationCode);

        MimeMessage message = createMimeMessage(mail, verificationCode);
        javaMailSender.send(message);
    }

    private MimeMessage createMimeMessage(String mail, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to create MimeMessage", e);
        }
        return message;
    }

    public boolean verifyMailCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(email); // Code used, remove it
            return true;
        }
        return false;
    }
}
