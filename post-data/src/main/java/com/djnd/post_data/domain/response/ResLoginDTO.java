package com.djnd.post_data.domain.response;

import com.djnd.post_data.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    // inner class
    public static class UserLogin {
        private Long id;
        private String email;
        private String name;
        private Role role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    // inner class
    public static class UserGetAccount {
        private UserLogin user;
    }

    // save information inside token (not save permission)
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    // inner class
    public static class UserInsideToken {
        private Long id;
        private String email;
        private String name;
    }

}
