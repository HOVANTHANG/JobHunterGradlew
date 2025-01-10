package com.example.JobHunter.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.JobHunter.domain.Company;
import com.example.JobHunter.domain.dto.CompanyCriteriaDTO;
import com.example.JobHunter.domain.dto.ResultPaginationDTO;
import com.example.JobHunter.service.CompanyService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.createdCompany(company));
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(CompanyCriteriaDTO companyCriteriaDTO) {

        int page = 1;
        int pageSizeInt = 10;
        try {
            if (companyCriteriaDTO.getPageSize().isPresent()) {
                pageSizeInt = Integer.parseInt(companyCriteriaDTO.getPageSize().get());

            }
            if (companyCriteriaDTO.getPageOptionl().isPresent()) {
                page = Integer.parseInt(companyCriteriaDTO.getPageOptionl().get());
            } else {

            }
        } catch (Exception e) {
        }

        Pageable pageable = PageRequest.of(page - 1, pageSizeInt);

        return ResponseEntity.ok(this.companyService.handleGetAllCompany(pageable));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        return ResponseEntity.ok(
                this.companyService.fetchCompanyByID(id).isPresent() ? this.companyService.fetchCompanyByID(id).get()
                        : null);
    }

    @PutMapping("/update_company")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(this.companyService.updateCompany(company));
    }

    @DeleteMapping("/delete_company/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable long id) {
        this.companyService.deleteCompany(id);
        return ResponseEntity.ok("Delete company success");
    }

}
