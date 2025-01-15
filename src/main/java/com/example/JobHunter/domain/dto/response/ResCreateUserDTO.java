package com.example.JobHunter.domain.dto.response;

import java.time.Instant;

import com.example.JobHunter.Util.constant.GenderEnum;
import com.example.JobHunter.domain.Company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private ResCompanyDTO company;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResCompanyDTO {
        private long id;
        private String name;
    }
}
