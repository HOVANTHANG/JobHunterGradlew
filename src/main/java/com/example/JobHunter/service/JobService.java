package com.example.JobHunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.JobHunter.domain.Job;
import com.example.JobHunter.domain.Skill;
import com.example.JobHunter.domain.dto.response.ResCreateJobDTO;
import com.example.JobHunter.domain.dto.response.ResJobDTO;
import com.example.JobHunter.domain.dto.response.ResUpdateJobDTO;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.repository.JobRepository;
import com.example.JobHunter.repository.SkillRepository;

import jakarta.validation.Valid;

@Service
public class JobService {

    private JobRepository jobRepository;
    private SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public Job handleCreateJob(Job job) {

        if (job.getSkills() != null) {
            List<Skill> listSkill = new ArrayList<Skill>();
            List<Long> listId = new ArrayList<Long>();
            for (Skill x : job.getSkills()) {
                listId.add(x.getId());
            }
            listSkill = this.skillRepository.findByIdIn(listId);
            job.setSkills(listSkill);
        }
        return this.jobRepository.save(job);

    }

    public ResCreateJobDTO convertToJobCreateDTO(Job job) {
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO();
        resCreateJobDTO.setId(job.getId());
        resCreateJobDTO.setName(job.getName());
        resCreateJobDTO.setLocation(job.getLocation());
        resCreateJobDTO.setSalary(job.getSalary());
        resCreateJobDTO.setQuantity(job.getQuantity());
        resCreateJobDTO.setLevel(job.getLevel());
        resCreateJobDTO.setStartDate(job.getStartDate());
        resCreateJobDTO.setEndDate(job.getEndDate());
        resCreateJobDTO.setActive(job.isActive());
        resCreateJobDTO.setCreatedAt(job.getCreatedAt());
        resCreateJobDTO.setCreatedBy(job.getCreatedBy());

        List<String> listStringSkill = new ArrayList<String>();
        for (Skill x : job.getSkills()) {
            listStringSkill.add(x.getName());
        }
        resCreateJobDTO.setSkills(listStringSkill);

        return resCreateJobDTO;
    }

    public Job handleUpdateJob(Job newJob) {
        Job oldJob = new Job();
        if (newJob != null) {
            oldJob = this.jobRepository.findById(newJob.getId()).isPresent()
                    ? this.jobRepository.findById(newJob.getId()).get()
                    : null;
            oldJob.setName(newJob.getName());
            oldJob.setLocation(newJob.getLocation());
            oldJob.setSalary(newJob.getSalary());
            oldJob.setQuantity(newJob.getQuantity());
            oldJob.setLevel(newJob.getLevel());
            oldJob.setDescription(newJob.getDescription());
            oldJob.setStartDate(newJob.getStartDate());
            oldJob.setEndDate(newJob.getEndDate());
            oldJob.setActive(newJob.isActive());

            List<Skill> listSkill = new ArrayList<Skill>();
            List<Long> listId = new ArrayList<Long>();
            for (Skill x : newJob.getSkills()) {
                listId.add(x.getId());
            }
            listSkill = this.skillRepository.findByIdIn(listId);
            oldJob.setSkills(listSkill);
        }
        return this.jobRepository.save(oldJob);
    }

    public ResUpdateJobDTO convertToJobUpdateDTO(Job job) {

        ResUpdateJobDTO resUpdateJobDTO = new ResUpdateJobDTO();
        resUpdateJobDTO.setId(job.getId());
        resUpdateJobDTO.setName(job.getName());
        resUpdateJobDTO.setLocation(job.getLocation());
        resUpdateJobDTO.setSalary(job.getSalary());
        resUpdateJobDTO.setQuantity(job.getQuantity());
        resUpdateJobDTO.setLevel(job.getLevel());
        resUpdateJobDTO.setStartDate(job.getStartDate());
        resUpdateJobDTO.setEndDate(job.getEndDate());
        resUpdateJobDTO.setActive(job.isActive());
        resUpdateJobDTO.setUpdateAt(job.getUpdatedAt());
        resUpdateJobDTO.setUpdateBy(job.getUpdatedBy());

        List<String> listStringSkill = new ArrayList<String>();
        for (Skill x : job.getSkills()) {
            listStringSkill.add(x.getName());
        }
        resUpdateJobDTO.setSkills(listStringSkill);

        return resUpdateJobDTO;
    }

    public Job handleFetchJobById(long id) {

        return this.jobRepository.findById(id).isPresent() ? this.jobRepository.findById(id).get() : null;
    }

    public ResJobDTO convertToJobDTO(Job job) {

        ResJobDTO resJobDTO = new ResJobDTO();

        List<Skill> listSkill = new ArrayList<Skill>();
        List<Long> listId = new ArrayList<Long>();
        for (Skill x : job.getSkills()) {
            listId.add(x.getId());
        }

        List<ResJobDTO.SkillDTO> list = new ArrayList<ResJobDTO.SkillDTO>();
        listSkill = this.skillRepository.findByIdIn(listId);
        for (Skill x : listSkill) {
            ResJobDTO.SkillDTO skill = new ResJobDTO.SkillDTO();
            skill.setId(x.getId());
            skill.setName(x.getName());
            list.add(skill);
        }

        resJobDTO.setId(job.getId());
        resJobDTO.setName(job.getName());
        resJobDTO.setLocation(job.getLocation());
        resJobDTO.setSalary(job.getSalary());
        resJobDTO.setQuantity(job.getQuantity());
        resJobDTO.setLevel(job.getLevel());
        resJobDTO.setStartDate(job.getStartDate());
        resJobDTO.setEndDate(job.getEndDate());
        resJobDTO.setActive(job.isActive());
        resJobDTO.setCreatedAt(job.getCreatedAt());
        resJobDTO.setCreatedBy(job.getCreatedBy());
        resJobDTO.setUpdatedAt(job.getUpdatedAt());
        resJobDTO.setUpdatedBy(job.getUpdatedBy());
        resJobDTO.setSkills(list);

        return resJobDTO;

    }

    public ResultPaginationDTO handlerFetchALlJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> listJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(listJob.getNumber() + 1);
        meta.setPageSize(listJob.getSize());

        meta.setPages(listJob.getTotalPages());
        meta.setTotal(listJob.getTotalElements());

        List<ResJobDTO> list = new ArrayList<ResJobDTO>();
        for (Job job : listJob.getContent()) {
            list.add(this.convertToJobDTO(job));
        }

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(list);

        return resultPaginationDTO;
    }

    public void handleDeleteJob(long id) {
        Job job = this.jobRepository.findById(id).isPresent() ? this.jobRepository.findById(id).get() : null;
        if (job != null) {
            this.jobRepository.delete(job);
        }
    }

}
