package com.example.JobHunter.controller.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JobHunter.Util.annotation.ApiMessage;
import com.example.JobHunter.domain.Skill;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.service.SkillService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> CreateSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> UpdateSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.ok().body(this.skillService.handleUpdateSkill(skill));
    }

    @GetMapping("/skills")
    @ApiMessage("Get ALL Skill")
    public ResponseEntity<ResultPaginationDTO> FetchALLSKill(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.skillService.handleFetchALLSkill(spec, pageable));
    }

    @DeleteMapping("/delete_skill/{id}")
    @ApiMessage("Delete Skill")
    public ResponseEntity<Void> DeleteSkill(@PathVariable("id") long id) {
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

}
