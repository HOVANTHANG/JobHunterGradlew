package com.example.JobHunter.controller.admin;

import com.example.JobHunter.Util.annotation.ApiMessage;
import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.response.ResCreateUserDTO;
import com.example.JobHunter.domain.dto.response.ResUpdateUserDTO;
import com.example.JobHunter.domain.dto.response.ResUserDTO;
import com.example.JobHunter.domain.dto.response.RestResponse;
import com.example.JobHunter.domain.dto.response.ResultPaginationDTO;
import com.example.JobHunter.service.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> create(@Valid @RequestBody User user) {
        // return this.userService.createUser(user);
        if (this.userService.existsByEmail(user.getEmail())) {
            throw new UsernameNotFoundException("Email " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userOld = this.userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToUserCreateDTO(userOld));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(this.userService.getallUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable("id") long id) {
        if (this.userService.fetchUserByid(id) == null) {
            throw new UsernameNotFoundException("Not found user with id: " + id);
        }
        User user = this.userService.getUserById(id);
        return ResponseEntity.ok(this.userService.convertToUserDTO(user));
    }

    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) {
        if (user != null) {
            if (this.userService.fetchUserByid(user.getId()) == null) {
                throw new UsernameNotFoundException("Not found user with id: " + user.getId());
            }
        }
        User userOld = this.userService.updateUser(user);
        return ResponseEntity.ok(this.userService.convertToUserUpdateDTO(userOld));
    }

    @DeleteMapping("/users/delete/{id}")
    @ApiMessage("Delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {

        if (this.userService.fetchUserByid(id) == null) {
            throw new UsernameNotFoundException("Not found user with id: " + id);

        }
        return ResponseEntity.ok(this.userService.deleteUserByid(id));
    }

}
