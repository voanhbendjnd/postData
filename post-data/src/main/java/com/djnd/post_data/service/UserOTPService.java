package com.djnd.post_data.service;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.entity.UserOTP;
import com.djnd.post_data.repository.UserOTPRepository;
import com.djnd.post_data.utils.SecurityUtils;

@Service
public class UserOTPService {
    private final UserOTPRepository userOTPRepository;
    private final UserService userService;

    public UserOTPService(UserOTPRepository userOTPRepository, UserService userService) {
        this.userOTPRepository = userOTPRepository;
        this.userService = userService;
    }

    public void save(UserOTP userOTP) {
        this.userOTPRepository.save(userOTP);
    }

    public int sendOTPCode(String email) {
        User user = this.userService.handleGetUserByUsername(email);
        Random rd = new Random();
        if (this.userService.existsByEmail(email) && user.getUserOTP() == null) {
            int currentdb = rd.nextInt(100000, 999999);
            UserOTP userOTP = new UserOTP();
            userOTP.setCode(currentdb);
            userOTP.setUser(user);
            this.userOTPRepository.save(userOTP);
            return currentdb;
        } else if (user.getUserOTP() != null) {
            int currentdb = rd.nextInt(100000, 999999);
            UserOTP userOTP = user.getUserOTP();
            userOTP.setCode(currentdb);
            this.userOTPRepository.save(userOTP);
            return currentdb;
        }
        return 0;
    }
}
