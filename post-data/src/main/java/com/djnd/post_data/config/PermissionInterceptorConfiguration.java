package com.djnd.post_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    // everyone access
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/",
                "/api/v1/auth/account",
                "/storage/**",
                "/api/v1/files",
                "/api/v1/subscribers/**",
                "/api/v1/email/**",
                "/files",
                "/carts/**",
                "/client/**",
                "/api/v1/client/auth/change-password",
                "/api/v1/client/auth/refresh"

        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
