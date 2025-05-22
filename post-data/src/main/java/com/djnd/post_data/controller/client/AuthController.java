package com.djnd.post_data.controller.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.request.user.RequestLoginDTO;
import com.djnd.post_data.domain.response.ResLoginDTO;
import com.djnd.post_data.domain.response.crud.ResUserCreateDTO;
import com.djnd.post_data.service.EmailService;
import com.djnd.post_data.service.UserService;
import com.djnd.post_data.utils.SecurityUtils;
import com.djnd.post_data.utils.annotation.ApiMessage;
import com.djnd.post_data.utils.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
        private final SecurityUtils securityUtils;
        private final AuthenticationManagerBuilder builder;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;
        private final EmailService emailService;
        @Value("${djnd.jwt.access-token-validity-in-seconds}")
        private Long refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder builder, SecurityUtils securityUtils,
                        UserService userService, PasswordEncoder passwordEncoder,
                        EmailService emailService) {
                this.builder = builder;
                this.securityUtils = securityUtils;
                this.userService = userService;
                this.emailService = emailService;
                this.passwordEncoder = passwordEncoder;
        }

        // access 15
        // refresh 30
        @PostMapping("/auth/login")
        @ApiMessage("Login account")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody RequestLoginDTO dto) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                dto.getUsername(), dto.getPassword());
                Authentication authentication = builder.getObject().authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                ResLoginDTO res = new ResLoginDTO();
                User user = this.userService.handleGetUserByUsername(dto.getUsername());
                if (user != null) {
                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(),
                                        user.getName(),
                                        user.getRole());
                        res.setUser(userLogin);
                }
                // create token
                String accessToken = this.securityUtils.createAccessToken(authentication.getName(), res);
                res.setAccessToken(accessToken);

                // create refresh token
                String refreshToken = this.securityUtils.createRefreshToken(dto.getUsername(), res);

                // update
                this.userService.updateUserToken(refreshToken, dto.getUsername());

                // set cookies
                ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(res);

        }

        @GetMapping("/auth/account")
        @ApiMessage("Fetch Account")
        public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
                String email = SecurityUtils.getCurrentUserLogin().get();
                User user = this.userService.handleGetUserByUsername(email);
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                ResLoginDTO.UserGetAccount userAccount = new ResLoginDTO.UserGetAccount();
                if (user != null) {
                        userLogin.setId(user.getId());
                        userLogin.setEmail(user.getEmail());
                        userLogin.setName(user.getName());
                        userLogin.setRole(user.getRole());
                        userAccount.setUser(userLogin);
                }
                return ResponseEntity.ok(userAccount);
        }

        @GetMapping("/auth/refresh")
        @ApiMessage("Create new refresh token")
        public ResponseEntity<ResLoginDTO> createReFreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "not") String refreshToken)
                        throws IdInvalidException {
                if (refreshToken.equals("not")) {
                        throw new IdInvalidException(">>> Not find refresh token");
                }
                // decode token get email
                Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refreshToken);
                String email = decodedToken.getSubject();
                User user = this.userService.fetchUserByRefreshTokenAndEmail(refreshToken, email);
                if (user == null) {
                        throw new IdInvalidException(">>> Token wrong <<<");
                }
                ResLoginDTO res = new ResLoginDTO();
                User user2 = this.userService.handleGetUserByUsername(email);
                if (user2 != null) {
                        ResLoginDTO.UserLogin aluka = new ResLoginDTO.UserLogin(user2.getId(), user2.getEmail(),
                                        user2.getName(), user2.getRole());
                        res.setUser(aluka);
                }
                // create token
                String access_token = this.securityUtils.createAccessToken(email, res);
                res.setAccessToken(access_token);
                // create token re
                String new_refreshToken = this.securityUtils.createRefreshToken(email, res);
                // update
                this.userService.updateUserToken(new_refreshToken, email);
                // set cookies
                ResponseCookie cookie = ResponseCookie.from("refresh_token", new_refreshToken)
                                .httpOnly(true) // chi chap nhan moi server nay
                                .secure(true) // chi nhan https
                                .path("/") // cho phep gui cookies cho tat ca giao thuc
                                .maxAge(refreshTokenExpiration) // thoi gian het han

                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(res);

        }

        @PutMapping("/auth/logout")
        @ApiMessage("Logout and delete refreshtoken")
        public ResponseEntity<Void> logoutAndDeleteRefreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "not") String refreshToken)
                        throws IdInvalidException {
                if (refreshToken.equals("not")) {
                        throw new IdInvalidException(">>> not find refresh token <<<");
                }
                Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refreshToken);
                String email = decodedToken.getSubject();
                User user = this.userService.fetchUserByRefreshTokenAndEmail(refreshToken, email);
                if (user == null) {
                        throw new IdInvalidException(">>> Wrong refresh token <<<");
                } else {
                        user.setRefreshToken(null);
                        this.userService.update(user);
                        return ResponseEntity.ok(null);
                }

        }

        // same ob
        @PostMapping("auth/logout")
        @ApiMessage("Logout and delete token")
        public ResponseEntity<Void> postLogout2() throws IdInvalidException {
                String email = SecurityUtils.getCurrentUserLogin().isPresent()
                                ? SecurityUtils.getCurrentUserLogin().get()
                                : "";
                if (email.equals("")) {
                        throw new IdInvalidException("Access Token không hợp lệ");
                }
                // update
                this.userService.updateUserToken(null, email);

                // remove
                ResponseCookie cookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(null);
        }

        @PostMapping("/auth/register")
        @ApiMessage("Create account")
        public ResponseEntity<ResUserCreateDTO> createAccount(@Valid @RequestBody User user) throws IdInvalidException {
                if (this.userService.existsByEmail(user.getEmail())) {
                        throw new IdInvalidException(">>> Email already exist, please choice other email! <<<");
                }
                user.setPassword(this.passwordEncoder.encode(user.getPassword()));
                return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createNewAccount(user));
        }

        @PostMapping("/auth/forget-password")
        @ApiMessage("Get OTP")
        public ResponseEntity<Void> getOPTForForgetPassword(@RequestBody RequestLoginDTO dto)
                        throws IdInvalidException {
                if (this.userService.existsByEmail(dto.getUsername())) {

                }
                throw new IdInvalidException(">>> Email or user name not exist! <<<");
        }

}
