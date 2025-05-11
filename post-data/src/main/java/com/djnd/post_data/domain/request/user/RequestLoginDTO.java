package com.djnd.post_data.domain.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginDTO {
    @NotBlank(message = "username must not empty")
    private String username;
    @NotBlank(message = "password must not empty")
    private String password;
}
