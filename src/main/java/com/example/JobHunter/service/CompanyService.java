package com.example.JobHunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.JobHunter.domain.Company;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createdCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleGetAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> companies = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(companies.getNumber() + 1);
        meta.setPageSize(companies.getSize());

        meta.setPages(companies.getTotalPages());
        meta.setTotal(companies.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(companies.getContent());

        return resultPaginationDTO;
    }

    public Optional<Company> fetchCompanyByID(long id) {

        return this.companyRepository.findById(id);
    }

    public Company updateCompany(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company companyUpdate = companyOptional.get();
            companyUpdate.setName(company.getName());
            companyUpdate.setAddress(company.getAddress());
            companyUpdate.setDescription(company.getDescription());
            companyUpdate.setLogo(company.getLogo());

            return this.companyRepository.save(companyUpdate);
        } else {
            return null;

        }
    }

    public void deleteCompany(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            this.companyRepository.delete(companyOptional.get());
        } else {
            return;
        }

    }

    public Optional<Company> fetchCompanyByID(Long id) {
        return companyRepository.findById(id);
    }

}
