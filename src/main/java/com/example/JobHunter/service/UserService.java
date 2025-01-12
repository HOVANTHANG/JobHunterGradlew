package com.example.JobHunter.service;

import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.Meta;
import com.example.JobHunter.domain.dto.ResultPaginationDTO;
import com.example.JobHunter.domain.dto.UserCreateDTO;
import com.example.JobHunter.domain.dto.UserDTO;
import com.example.JobHunter.domain.dto.UserLoginDTO;
import com.example.JobHunter.domain.dto.UserUpdateDTO;
import com.example.JobHunter.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {

        if (user != null) {
            return this.userRepository.save(user);
        } else {
            return null;
        }
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPaginationDTO getallUser(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(users.getNumber() + 1);
        meta.setPageSize(users.getSize());

        meta.setPages(users.getTotalPages());
        meta.setTotal(users.getTotalElements());

        List<UserDTO> userList = new ArrayList<UserDTO>();

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

    public UserCreateDTO convertToUserCreateDTO(User userOld) {
        UserCreateDTO userDTO = new UserCreateDTO();
        userDTO.setId(userOld.getId());
        userDTO.setName(userOld.getName());
        userDTO.setEmail(userOld.getEmail());
        userDTO.setGender(userOld.getGender());
        userDTO.setCreatedAt(userOld.getCreatedAt());
        userDTO.setAddress(userOld.getAddress());
        userDTO.setAge(userOld.getAge());

        return userDTO;
    }

    public User fetchUserByid(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public UserUpdateDTO convertToUserUpdateDTO(User userOld) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(userOld.getId());
        userUpdateDTO.setName(userOld.getName());
        userUpdateDTO.setEmail(userOld.getEmail());
        userUpdateDTO.setGender(userOld.getGender());
        userUpdateDTO.setUpdateAt(userOld.getUpdatedAt());
        userUpdateDTO.setAddress(userOld.getAddress());
        userUpdateDTO.setAge(userOld.getAge());
        return userUpdateDTO;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setAddress(user.getAddress());
        userDTO.setAge(user.getAge());

        return userDTO;

    }

    public User fetchUserByEmail(String username) {

        return this.userRepository.findByEmail(username);
    }

    public UserLoginDTO convertToUserLoginDTO(User user) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setId(user.getId());
        userLoginDTO.setName(user.getName());
        userLoginDTO.setEmail(user.getEmail());
        return userLoginDTO;
    }

}
