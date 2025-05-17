package com.djnd.post_data.domain.response.crud;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResPermissionUpdateDTO {
    private Long id;
    private String name;
    private String module;
    private String apiPath;
    private String method;
    private Instant updatedAt;
    private String updatedBy;
    private List<Role> roles;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Role {
        private Long id;
        private String name;
    }
}
