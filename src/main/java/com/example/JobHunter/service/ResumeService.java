package com.example.JobHunter.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.JobHunter.Util.constant.StatusEnum;
import com.example.JobHunter.domain.Job;
import com.example.JobHunter.domain.Resume;
import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO.Meta;
import com.example.JobHunter.domain.dto.response.resume.ResCreateResume;
import com.example.JobHunter.domain.dto.response.resume.ResResumeDTO;
import com.example.JobHunter.domain.dto.response.resume.ResResumeDTO.JobDTO;
import com.example.JobHunter.domain.dto.response.resume.ResResumeDTO.UserDTO;
import com.example.JobHunter.domain.dto.response.resume.ResUpdateResumeDTO;
import com.example.JobHunter.repository.JobRepository;
import com.example.JobHunter.repository.ResumeRepository;
import com.example.JobHunter.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class ResumeService {

    private ResumeRepository resumeRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public Resume handleCreateResume(Resume resume) {
        if (resume != null) {
            Job job = this.jobRepository.findById(resume.getJob().getId()).isPresent()
                    ? this.jobRepository.findById(resume.getJob().getId()).get()
                    : null;
            User user = this.userRepository.findById(resume.getUser().getId()).isPresent()
                    ? this.userRepository.findById(resume.getUser().getId()).get()
                    : null;
            if (job == null || user == null) {
                throw new UsernameNotFoundException("Don't exist User_id Or Job_id");
            }
            resume.setJob(job);
            resume.setUser(user);
        }

        return this.resumeRepository.save(resume);
    }

    public ResCreateResume convertCreateResumeDTO(Resume oldResume) {
        ResCreateResume resCreateResume = new ResCreateResume();
        resCreateResume.setId(oldResume.getId());
        resCreateResume.setCreateAt(Instant.now());
        resCreateResume.setCreateBy(oldResume.getCreatedBy());
        return resCreateResume;
    }

    public ResUpdateResumeDTO convertUpdateResumeDTO(Resume oldResume) {
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO();
        resUpdateResumeDTO.setUpdateAt(Instant.now());
        resUpdateResumeDTO.setUpdateBy(oldResume.getUpdatedBy());

        return resUpdateResumeDTO;
    }

    public Resume handleUpdateResume(Resume resume) {
        Resume oldResume = new Resume();
        if (resume != null) {
            oldResume = this.resumeRepository.findById(resume.getId()).isPresent()
                    ? this.resumeRepository.findById(resume.getId()).get()
                    : null;
            if (oldResume == null) {
                throw new UsernameNotFoundException("Id isn't exist!");
            }
            oldResume.setStatus(resume.getStatus());
        }
        return this.resumeRepository.save(oldResume);
    }

    public void handleDeleteResume(Long id) {

        Resume oldResume = this.resumeRepository.findById(id).isPresent()
                ? this.resumeRepository.findById(id).get()
                : null;
        if (oldResume == null) {
            throw new UsernameNotFoundException("Id isn't exist!");
        } else {
            this.resumeRepository.delete(oldResume);
        }
    }

    public Resume handleFetchResumeByID(Long id) {
        Resume oldResume = this.resumeRepository.findById(id).isPresent()
                ? this.resumeRepository.findById(id).get()
                : null;
        if (oldResume == null) {
            throw new UsernameNotFoundException("Id isn't exist!");
        }
        return oldResume;
    }

    public ResResumeDTO convertResumeDTO(Resume findResume) {
        ResResumeDTO resResumeDTO = new ResResumeDTO();
        ResResumeDTO.UserDTO userDTO = new ResResumeDTO.UserDTO();
        ResResumeDTO.JobDTO jobDTO = new ResResumeDTO.JobDTO();
        if (findResume != null) {
            resResumeDTO.setId(findResume.getId());
            resResumeDTO.setEmail(findResume.getEmail());
            resResumeDTO.setUrl(findResume.getUrl());
            resResumeDTO.setCreatedAt(findResume.getCreatedAt());
            resResumeDTO.setCreatedBy(findResume.getCreatedBy());
            resResumeDTO.setStatus(findResume.getStatus());
            resResumeDTO.setUpdatedAt(findResume.getUpdatedAt());
            resResumeDTO.setUpdatedBy(findResume.getUpdatedBy());

            userDTO.setId(findResume.getUser().getId());
            userDTO.setName(findResume.getUser().getName());
            jobDTO.setId(findResume.getJob().getId());
            jobDTO.setName(findResume.getJob().getName());
        }
        resResumeDTO.setUser(userDTO);
        resResumeDTO.setJob(jobDTO);

        return resResumeDTO;
    }

    public ResultPaginationDTO handleFetchALLResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumes = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(resumes.getNumber() + 1);
        meta.setPageSize(resumes.getSize());

        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());

        List<ResResumeDTO> listResumes = new ArrayList<ResResumeDTO>();
        for (Resume resume : resumes.getContent()) {
            listResumes.add(this.convertResumeDTO(resume));
        }
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(listResumes);
        return resultPaginationDTO;
    }

}
