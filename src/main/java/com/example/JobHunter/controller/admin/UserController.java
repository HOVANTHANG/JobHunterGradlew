package com.example.JobHunter.controller.admin;

import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.ResultPaginationDTO;
import com.example.JobHunter.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        // return this.userService.createUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(user));
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(this.userService.getallUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PutMapping("/users/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(this.userService.updateUser(user));
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        return ResponseEntity.ok(this.userService.deleteUserByid(id));
    }

}
