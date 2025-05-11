package com.djnd.post_data.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.request.user.RequestCreateDTO;
import com.djnd.post_data.domain.response.crud.ResUserCreateDTO;
import com.djnd.post_data.service.RoleService;
import com.djnd.post_data.service.UserService;
import com.djnd.post_data.utils.annotation.ApiMessage;
import com.djnd.post_data.utils.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
            RoleService roleService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create new user")
    public ResponseEntity<ResUserCreateDTO> createUser(@Valid @RequestBody RequestCreateDTO user)
            throws IdInvalidException {
        if (this.userService.existsByEmail(user.getEmail())) {
            throw new IdInvalidException(">>> User with email have existed <<<");
        }
        if (this.roleService.findByName(user.getRole()) == null) {
            throw new IdInvalidException(">>> Role with name  " + user.getRole() + " not found! <<<");
        }
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createNewUser(user));

    }
}
