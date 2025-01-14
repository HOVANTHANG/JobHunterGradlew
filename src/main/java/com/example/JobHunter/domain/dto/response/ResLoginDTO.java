package com.example.JobHunter.domain.dto.response;

import com.example.JobHunter.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    @JsonProperty("access_token")
    private String access_token;

    private UserLoginDTO user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginDTO {
        private long id;
        private String name;
        private String email;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetAccountDTO {
        private UserLoginDTO user;
    }

}
