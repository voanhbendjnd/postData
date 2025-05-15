package com.djnd.post_data.domain.response.crud;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResRoleUpdateDTO {
    private Long id;
    private String name;
    private boolean active;
    private String description;
    private Instant updatedAt;
    private String updatedBy;
    private List<permission> permission;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class permission {
        private Long id;
        private String name;
    }

}