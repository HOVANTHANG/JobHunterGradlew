package com.example.JobHunter.domain.dto.response;

import java.time.Instant;

import com.example.JobHunter.Util.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;

    private String name;

    private String email;

    private int age;
    private GenderEnum gender;

    private String address;

    private Instant updateAt;

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
