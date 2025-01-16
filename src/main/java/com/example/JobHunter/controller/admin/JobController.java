package com.example.JobHunter.controller.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JobHunter.Util.annotation.ApiMessage;
import com.example.JobHunter.domain.Job;
import com.example.JobHunter.domain.dto.response.ResCreateJobDTO;
import com.example.JobHunter.domain.dto.response.ResJobDTO;
import com.example.JobHunter.domain.dto.response.ResUpdateJobDTO;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.service.JobService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a new Job")
    public ResponseEntity<ResCreateJobDTO> CreateJob(@Valid @RequestBody Job job) {

        Job newjob = this.jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.convertToJobCreateDTO(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) {

        Job newjob = this.jobService.handleUpdateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.convertToJobUpdateDTO(newjob));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch Job By ID")
    public ResponseEntity<ResJobDTO> fetchjobByid(@PathVariable("id") long id) {

        Job job = this.jobService.handleFetchJobById(id);
        if (job == null) {
            throw new UsernameNotFoundException("Don't exist id in system!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.convertToJobDTO(job));
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all job")
    public ResponseEntity<ResultPaginationDTO> fetchalljobBy(@Filter Specification<Job> spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.jobService.handlerFetchALlJob(spec, pageable));
    }

    @DeleteMapping("/delete_job/{id}")
    @ApiMessage("Delete Job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) {
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok().body(null);
    }

}
