package com.example.JobHunter.domain.dto.response.job;

import java.time.Instant;
import java.util.List;

import com.example.JobHunter.Util.constant.LevelEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;

    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean Active;
    private Instant updateAt;

    private String updateBy;

    private List<String> skills;
}
