package com.djnd.post_data.domain.response.crud;

import java.time.Instant;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResPermissionCreateDTO {
    private Long id;
    private String name;
    private String module;
    private String apiPath;
    private Instant createdAt;
    private String createdBy;
    private String method;
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
