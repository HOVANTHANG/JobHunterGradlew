package com.example.JobHunter.domain.dto.response.job;

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
public class ResJobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;

    private LevelEnum level;

    private String description;

    private Instant startDate;
    private Instant endDate;
    private boolean Active;
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    private List<SkillDTO> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillDTO {
        private long id;
        private String name;
    }

}
