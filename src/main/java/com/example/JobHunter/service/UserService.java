package com.example.JobHunter.service;

import com.example.JobHunter.domain.User;
import com.example.JobHunter.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return this.userRepository.save(user);
    }

    public List<User> getallUser() {
        return this.userRepository.findAll();
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
            olduser.setEmail(user.getEmail());
            olduser.setPassword(passwordEncoder.encode(user.getPassword()));

            this.userRepository.save(olduser);
        }
        return olduser;
    }

    public String deleteUserByid(long id) {
        User user = this.getUserById(id);
        if (user != null) {
            this.userRepository.delete(user);
            return "User deleted";
        } else {
            return "User not found";
        }
    }

    public User handlerGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
