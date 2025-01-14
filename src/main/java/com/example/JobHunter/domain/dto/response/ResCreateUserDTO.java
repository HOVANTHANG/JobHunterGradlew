package com.example.JobHunter.domain.dto.response;

import java.time.Instant;

import com.example.JobHunter.Util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;

    private String name;

    private String email;

    private int age;
    private GenderEnum gender;

    private String address;

    private Instant createdAt;

}
