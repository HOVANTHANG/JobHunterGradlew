package com.example.JobHunter.domain.dto;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCriteriaDTO {
    private Optional<String> name;
    private Optional<String> pageOptionl;
    private Optional<String> pageSize;

}
