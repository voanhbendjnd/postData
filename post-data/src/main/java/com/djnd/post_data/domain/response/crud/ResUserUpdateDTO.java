package com.djnd.post_data.domain.response.crud;

import java.time.Instant;

import com.djnd.post_data.utils.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserUpdateDTO {
    private Long id;
    private String email;
    private String name;
    private Integer age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private String updatedBy;
}
