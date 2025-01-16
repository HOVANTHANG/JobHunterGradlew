package com.example.JobHunter.domain.dto.response;

import java.time.Instant;
import java.util.List;

import com.example.JobHunter.Util.constant.LevelEnum;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResCreateJobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;

    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean Active;
    private Instant createdAt;

    private String createdBy;

    private List<String> skills;

    // @Getter
    // @Setter
    // @NoArgsConstructor
    // @AllArgsConstructor
    // public static class GetSkill {
    // private List<String> skills;
    // }
}
