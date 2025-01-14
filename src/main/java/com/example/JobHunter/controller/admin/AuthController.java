package com.example.JobHunter.controller.admin;

import com.example.JobHunter.Util.SecurityUtil;
import com.example.JobHunter.Util.annotation.ApiMessage;
import com.example.JobHunter.domain.User;
import com.example.JobHunter.domain.dto.LoginDTO;
import com.example.JobHunter.domain.dto.ResLoginDTO;
import com.example.JobHunter.domain.dto.UserLoginDTO;
import com.example.JobHunter.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Value("${thangka.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    private UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // put input include username/password into Security
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword());
        // Authentication => Need write function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(auth);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        User user = this.userService.fetchUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            userLoginDTO = this.userService.convertToUserLoginDTO(user);
            res.setUser(userLoginDTO);
        }
        // create a Token
        String access_Token = this.securityUtil.CreateAccessToken(authentication.getName(), userLoginDTO);
        res.setAccess_token(access_Token);
        // Create refresh token
        String refresh_Token = this.securityUtil.CreateRefreshToken(loginDTO.getUsername(), userLoginDTO);
        // Update refresh token
        this.userService.updateRefreshToken(refresh_Token, userLoginDTO.getEmail());

        // Set cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refresh_Token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<ResLoginDTO.UserGetAccountDTO> getAccount() {

        // String email =
        // SecurityContextHolder.getContext().getAuthentication().getName();
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User user = this.userService.fetchUserByEmail(email);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        ResLoginDTO.UserGetAccountDTO userGetAccountDTO = new ResLoginDTO.UserGetAccountDTO();
        if (user != null) {
            userLoginDTO.setId(user.getId());
            userLoginDTO.setName(user.getName());
            userLoginDTO.setEmail(user.getEmail());
            userGetAccountDTO.setUser(userLoginDTO);
        }
        return ResponseEntity.ok(userGetAccountDTO);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Refresh new token")
    public ResponseEntity<ResLoginDTO> refresh_Token(
            @CookieValue("refresh_token") String token) {

        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(token);

        String email = decodedToken.getSubject();

        User user = this.userService.handlerGetUserByRefreshTokenAndEmail(token, email);
        if (user == null) {
            throw new UsernameNotFoundException("Refresh Token is inValid");
        }

        ResLoginDTO res = new ResLoginDTO();

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        User currentUser = this.userService.fetchUserByEmail(email);
        if (currentUser != null) {
            userLoginDTO = this.userService.convertToUserLoginDTO(currentUser);
            res.setUser(userLoginDTO);
        }
        // create a Token
        String access_Token = this.securityUtil.CreateAccessToken(email, userLoginDTO);
        res.setAccess_token(access_Token);
        // Create refresh token
        String new_refresh_Token = this.securityUtil.CreateRefreshToken(email, userLoginDTO);
        // Update refresh token
        this.userService.updateRefreshToken(new_refresh_Token, email);

        // Set cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", new_refresh_Token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() {
        String email = securityUtil.getCurrentUserLogin().isPresent() ? securityUtil.getCurrentUserLogin().get() : "";

        if (email == "") {
            throw new UsernameNotFoundException("Access Token is invalid");
        }
        this.userService.updateRefreshToken(null, email);

        // Set cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }

}
