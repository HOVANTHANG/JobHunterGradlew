package com.example.JobHunter.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import com.example.JobHunter.domain.Resume;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.domain.dto.response.resume.ResCreateResume;
import com.example.JobHunter.domain.dto.response.resume.ResResumeDTO;
import com.example.JobHunter.domain.dto.response.resume.ResUpdateResumeDTO;
import com.example.JobHunter.service.ResumeService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new Resume")
    public ResponseEntity<ResCreateResume> createResume(@Valid @RequestBody Resume resume) {

        Resume oldResume = this.resumeService.handleCreateResume(resume);

        return ResponseEntity.ok().body(this.resumeService.convertCreateResumeDTO(oldResume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a new Resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@Valid @RequestBody Resume resume) {

        Resume oldResume = this.resumeService.handleUpdateResume(resume);

        return ResponseEntity.ok().body(this.resumeService.convertUpdateResumeDTO(oldResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a  Resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) {

        this.resumeService.handleDeleteResume(id);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a  Resume by Id")
    public ResponseEntity<ResResumeDTO> getResumeByID(@PathVariable("id") Long id) {

        Resume findResume = this.resumeService.handleFetchResumeByID(id);

        return ResponseEntity.ok().body(this.resumeService.convertResumeDTO(findResume));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch ALL  Resume ")
    public ResponseEntity<ResultPaginationDTO> getALLResume(@Filter Specification spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.handleFetchALLResume(spec, pageable));
    }

}
