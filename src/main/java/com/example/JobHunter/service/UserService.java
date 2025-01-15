package com.example.JobHunter.service;

import com.example.JobHunter.domain.Company;
import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.response.ResCreateUserDTO;
import com.example.JobHunter.domain.dto.response.ResUpdateUserDTO;
import com.example.JobHunter.domain.dto.response.ResUserDTO;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private CompanyService companyService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyService companyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
    }

    public User createUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> company = this.companyService.fetchCompanyByID(user.getCompany().getId());
            user.setCompany(company.isPresent() ? company.get() : null);
        }
        return this.userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPaginationDTO getallUser(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(users.getNumber() + 1);
        meta.setPageSize(users.getSize());

        meta.setPages(users.getTotalPages());
        meta.setTotal(users.getTotalElements());

        List<ResUserDTO> userList = new ArrayList<ResUserDTO>();

        for (User user : users.getContent()) {
            userList.add(this.convertToUserDTO(user));
        }

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(userList);
        return resultPaginationDTO;
    }

    public User getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User updateUser(User user) {
        User olduser = this.getUserById(user.getId());
        if (olduser != null) {

            olduser.setName(user.getName());
            olduser.setGender(user.getGender());
            olduser.setAge(user.getAge());
            olduser.setAddress(user.getAddress());

            this.userRepository.save(olduser);
        }
        return olduser;
    }

    public Void deleteUserByid(long id) {
        this.userRepository.deleteById(id);
        return null;
    }

    public User handlerGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ResCreateUserDTO convertToUserCreateDTO(User userOld) {
        ResCreateUserDTO userDTO = new ResCreateUserDTO();
        userDTO.setId(userOld.getId());
        userDTO.setName(userOld.getName());
        userDTO.setEmail(userOld.getEmail());
        userDTO.setGender(userOld.getGender());
        userDTO.setCreatedAt(userOld.getCreatedAt());
        userDTO.setAddress(userOld.getAddress());
        userDTO.setAge(userOld.getAge());
        ResCreateUserDTO.ResCompanyDTO company = new ResCreateUserDTO.ResCompanyDTO();
        company.setId(userOld.getCompany().getId());
        company.setName(userOld.getCompany().getName());
        userDTO.setCompany(company);

        return userDTO;
    }

    public User fetchUserByid(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public ResUpdateUserDTO convertToUserUpdateDTO(User userOld) {
        ResUpdateUserDTO userUpdateDTO = new ResUpdateUserDTO();
        userUpdateDTO.setId(userOld.getId());
        userUpdateDTO.setName(userOld.getName());
        userUpdateDTO.setEmail(userOld.getEmail());
        userUpdateDTO.setGender(userOld.getGender());
        userUpdateDTO.setUpdateAt(userOld.getUpdatedAt());
        userUpdateDTO.setAddress(userOld.getAddress());
        userUpdateDTO.setAge(userOld.getAge());
        return userUpdateDTO;
    }

    public ResUserDTO convertToUserDTO(User user) {

        ResUserDTO userDTO = new ResUserDTO();
        ResUserDTO.ResCompanyDTO companyDTO = new ResUserDTO.ResCompanyDTO();
        companyDTO.setId(user.getCompany().getId());
        companyDTO.setName(user.getCompany().getName());

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setAddress(user.getAddress());
        userDTO.setAge(user.getAge());
        userDTO.setCompany(companyDTO);

        return userDTO;

    }

    public User fetchUserByEmail(String username) {

        return this.userRepository.findByEmail(username);
    }

    public void updateRefreshToken(String refresh_Token, String email) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(refresh_Token);
            this.userRepository.save(user);
        }
    }

    public User handlerGetUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
