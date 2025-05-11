package com.djnd.post_data.config;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.djnd.post_data.service.UserService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Component("userDetailsService")
public class UserDetailCustoms implements UserDetailsService {
    private final UserService userService;

    public UserDetailCustoms(UserService userService) {
        this.userService = userService;
    }

    // principal
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.djnd.post_data.domain.entity.User user = this.userService.handleGetUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username/password not found or exactly");
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
