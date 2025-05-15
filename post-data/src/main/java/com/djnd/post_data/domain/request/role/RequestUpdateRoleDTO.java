package com.djnd.post_data.domain.request.role;

import java.time.Instant;
import java.util.List;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.utils.SecurityUtils;

import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateRoleDTO {
    private String condition;
    private Long id;
    private String name;
    private Boolean active;
    private String description;
    private List<Permission> permissions;

}
