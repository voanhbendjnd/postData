package com.djnd.post_data.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.request.user.RequestCreateDTO;
import com.djnd.post_data.domain.response.ResultPaginationDTO;
import com.djnd.post_data.domain.response.crud.ResUserCreateDTO;
import com.djnd.post_data.domain.response.crud.ResUserUpdateDTO;
import com.djnd.post_data.service.RoleService;
import com.djnd.post_data.service.UserService;
import com.djnd.post_data.utils.annotation.ApiMessage;
import com.djnd.post_data.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

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

    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<ResUserUpdateDTO> updateUser(@Valid @RequestBody User user)
            throws IdInvalidException {
        if (!this.userService.existsById(user.getId())) {
            throw new IdInvalidException(">>> ID user not exist in system <<<");
        }
        return ResponseEntity.ok(this.userService.updateUser2(user));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch by id")
    public ResponseEntity<ResUserCreateDTO> fetchById(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.userService.existsById(id)) {
            throw new IdInvalidException(">>> ID user not exist in system <<<");
        }
        return ResponseEntity.ok(this.userService.fetchUserById(id));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all user")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok(this.userService.fetchAll(spec, pageable));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user by id")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.userService.existsById(id)) {
            throw new IdInvalidException(">>> ID user not exists in system <<<");
        }
        this.userService.deleteUserById(id);
        return ResponseEntity.ok(null);
    }

}
