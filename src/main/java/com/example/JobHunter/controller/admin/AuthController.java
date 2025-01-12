package com.example.JobHunter.controller.admin;

import com.example.JobHunter.Util.SecurityUtil;
import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.LoginDTO;
import com.example.JobHunter.domain.dto.ResLoginDTO;
import com.example.JobHunter.domain.dto.UserLoginDTO;
import com.example.JobHunter.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // put input include username/password into Security
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword());
        // Authentication => Need write function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(auth);

        // create a Token
        String access_Token = this.securityUtil.CreateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        res.setAccess_token(access_Token);

        User user = this.userService.fetchUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            UserLoginDTO userLoginDTO = this.userService.convertToUserLoginDTO(user);
            res.setUser(userLoginDTO);

        }

        return ResponseEntity.ok().body(res);
    }
}
