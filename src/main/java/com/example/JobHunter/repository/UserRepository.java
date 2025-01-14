package com.example.JobHunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.JobHunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String username);

    Page<User> findAll(Pageable pageable);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String refreshToken, String email);
}
