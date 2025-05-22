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

    public EmailService(MailSender mailSender, UserService userService) {
        this.userService = userService;
        this.mailSender = mailSender;
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
    public void sendOTPCode() {
        SimpleMailMessage msg = new SimpleMailMessage();
        String email = SecurityUtils.getCurrentUserLogin().get();
        User user = this.userService.handleGetUserByUsername(email);
        Random rd = new Random();
        if (this.userService.existsByEmail(email) && user.getUserOTP() == null) {
            int currentdb = rd.nextInt(100000, 999999);
            UserOTP userOTP = new UserOTP();
            userOTP.setCode(currentdb);

        }
    }
}
