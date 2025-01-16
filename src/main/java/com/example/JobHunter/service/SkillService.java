package com.example.JobHunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.JobHunter.domain.Job;
import com.example.JobHunter.domain.Skill;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.repository.SkillRepository;

@Service
public class SkillService {

    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) {
        if (this.skillRepository.existsByName(skill.getName())) {
            throw new UsernameNotFoundException("Name " + skill.getName() + " already exist!");
        }
        return this.skillRepository.save(skill);
    }

    public Skill handleUpdateSkill(Skill skill) {
        Skill oldSkill = this.FetchSkillById(skill.getId());
        if (oldSkill == null) {
            throw new UsernameNotFoundException("Id not found");
        }
        oldSkill.setName(skill.getName());
        return this.skillRepository.save(oldSkill);
    }

    public Skill FetchSkillById(long id) {
        return this.skillRepository.findById(id).isPresent() ? this.skillRepository.findById(id).get() : null;
    }

    public ResultPaginationDTO handleFetchALLSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skills = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(skills.getNumber() + 1);
        meta.setPageSize(skills.getSize());

        meta.setPages(skills.getTotalPages());
        meta.setTotal(skills.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(skills.getContent());

        return resultPaginationDTO;
    }

    public void handleDeleteSkill(long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        if (skill.isPresent()) {
            skill.get().getJobs().forEach(job -> {
                job.getSkills().remove(skill.get());
            });

            this.skillRepository.delete(skill.get());
        } else {
            throw new UsernameNotFoundException("Id not found");
        }
    }
}
