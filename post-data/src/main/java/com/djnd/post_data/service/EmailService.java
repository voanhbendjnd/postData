package com.djnd.post_data.service;

import java.util.Random;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.entity.UserOTP;
import com.djnd.post_data.utils.SecurityUtils;

@Service
public class EmailService {
    private final MailSender mailSender;
    private final UserService userService;
    private final UserOTPService userOTPService;

    public EmailService(MailSender mailSender, UserService userService, UserOTPService userOTPService) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.userOTPService = userOTPService;
    }

    @Async
    public void sendSimpleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("benva.ce190709@gmail.com");
        msg.setSubject(">>> Simple Mail <<<");
        msg.setText("Hi");
        this.mailSender.send(msg);
    }

    @Async
    public void sendOTPCode(int otp, String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("OTP khôi phục tài khoản");
        msg.setText(otp + "");
        this.mailSender.send(msg);
    }
}
