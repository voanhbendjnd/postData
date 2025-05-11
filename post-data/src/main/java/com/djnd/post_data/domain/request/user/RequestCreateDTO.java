package com.djnd.post_data.domain.request.user;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.utils.constant.GenderEnum;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateDTO {
    private Long id;
    private String name;
    @Size(min = 6, message = "Password must be at least 6")
    private String password;
    private String address;
    private Integer age;
    private GenderEnum gender;
    private String email;
    private String role;
}
