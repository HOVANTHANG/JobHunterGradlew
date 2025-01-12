package com.example.JobHunter.domain.dto;

import com.example.JobHunter.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    private String access_token;
    private UserLoginDTO user;
}
