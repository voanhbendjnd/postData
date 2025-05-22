package com.djnd.post_data.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djnd.post_data.service.EmailService;
import com.djnd.post_data.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    private final EmailService emailService;

    public HelloController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @PostMapping("/emails")
    @ApiMessage("Send email!")
    public ResponseEntity<Void> sendEmail() {
        this.emailService.sendSimpleEmail();
        return ResponseEntity.ok(null);
    }
}
