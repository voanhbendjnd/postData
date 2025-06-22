package com.djnd.post_data.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.entity.UserOTP;
import com.djnd.post_data.domain.response.ResLoginDTO;
import com.djnd.post_data.repository.UserOTPRepository;
import com.djnd.post_data.utils.SecurityUtils;

@Service
public class UserOTPService {
    private static final Long otp_time = 30L;
    private final UserOTPRepository userOTPRepository;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    public UserOTPService(UserOTPRepository userOTPRepository, UserService userService,
            SecurityUtils securityUtils) {
        this.userOTPRepository = userOTPRepository;
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    public void save(UserOTP userOTP) {
        this.userOTPRepository.save(userOTP);
    }

    private void removeOtpBy30s(String email) {
        User user = this.userService.handleGetUserByUsername(email);
        UserOTP otp = user.getUserOTP();
        if (otp != null) {
            if (Duration.between(otp.getCreatetionTime(), LocalDateTime.now()).getSeconds() >= otp_time) {
                otp.setCode(null);
                this.userOTPRepository.save(otp);
            }
        }
    }

    public int sendOTPCode(String email) {
        User user = this.userService.handleGetUserByUsername(email);
        Random rd = new Random();
        if (this.userService.existsByEmail(email) && user.getUserOTP() == null) {
            int currentdb = rd.nextInt(100000, 999999);
            UserOTP userOTP = new UserOTP();
            userOTP.setCreatetionTime(LocalDateTime.now());
            userOTP.setCode(currentdb);
            userOTP.setUser(user);
            this.userOTPRepository.save(userOTP);
            return currentdb;
        } else if (user.getUserOTP() != null) {
            this.removeOtpBy30s(email);
            int currentdb = rd.nextInt(100000, 999999);
            UserOTP userOTP = user.getUserOTP();
            userOTP.setCode(currentdb);
            userOTP.setCreatetionTime(LocalDateTime.now());
            this.userOTPRepository.save(userOTP);
            return currentdb;
        }
        return 0;
    }

    @Value("${djnd.jwt.access-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    @Transactional
    public String changePasswordForUser(String email, int code) {
        User user = this.userService.handleGetUserByUsername(email);
        if (user.getUserOTP() == null) {
            return ">>> You have not sent request OTP for change password! <<<";
        }
        if (user.getUserOTP().getCode() == code && user.getRefreshToken() != null) {
            // delete OTP object
            this.userOTPRepository.delete(user.getUserOTP());
            // annalyz jwt by refreshtoken
            Jwt decoded = this.securityUtils.checkValidRefreshToken(user.getRefreshToken());
            // get info from jwt
            String emailJwt = decoded.getSubject();
            User user86 = this.userService.fetchUserByRefreshTokenAndEmail(user.getRefreshToken(), emailJwt);
            if (user86 == null) {
                return ">>> Error, not found <<<";
            }
            ResLoginDTO res = new ResLoginDTO();
            User user92 = this.userService.handleGetUserByUsername(emailJwt);
            if (user92 != null) {
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user92.getId(), user92.getEmail(),
                        user92.getName(), user92.getRole());
                res.setUser(userLogin);

            }
            String newAccessToken = this.securityUtils.createAccessToken(emailJwt, res);
            res.setAccessToken(newAccessToken);
            String newRefreshToken = this.securityUtils.createRefreshToken(emailJwt, res);
            this.userService.updateUserToken(newRefreshToken, emailJwt);

            return "AccessToken: " + newAccessToken;
        }
        return ">>> OTP wrong! <<<";
    }
}
