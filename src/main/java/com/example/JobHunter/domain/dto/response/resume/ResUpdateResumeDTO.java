package com.example.JobHunter.domain.dto.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateResumeDTO {
    private Instant updateAt;
    private String updateBy;

}
