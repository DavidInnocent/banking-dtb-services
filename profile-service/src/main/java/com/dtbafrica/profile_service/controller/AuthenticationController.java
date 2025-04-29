package com.dtbafrica.profile_service.controller;


import com.dtbafrica.profile_service.dto.AuthLogin;
import com.dtbafrica.profile_service.dto.AuthLoginResponse;
import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.RefreshToken;
import com.dtbafrica.profile_service.model.UserInfo;
import com.dtbafrica.profile_service.repository.RefreshTokenRepository;
import com.dtbafrica.profile_service.service.AuthenticationUserDetailsService;
import com.dtbafrica.profile_service.service.JwtService;
import com.dtbafrica.profile_service.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    AuthenticationUserDetailsService authenticationUserDetailsService;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtService jwtService;
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthLogin authLogin) {
        UserInfo userDetails = (UserInfo) authenticationUserDetailsService.loadUserByUsername(authLogin.getEmail());
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLogin.getEmail(),
                        authLogin.getPassword()));
        
        if (authentication.isAuthenticated()) {
            return generateAuthResponse(userDetails);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
    
    public ResponseEntity<AuthLoginResponse> generateAuthResponse(UserInfo userDetails) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getEmail());
        AuthLoginResponse authenticationResponse = AuthLoginResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .expiration(refreshToken.getExpiration().toString())
                .accessToken(jwtService.generateToken(userDetails.getUsername(),
                        Date.from(Instant.now().plusMillis(1000 * 60 * 60 * 24))))
                .build();
        return ResponseEntity.ok(authenticationResponse);
    }
    
   
    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRegister authRegister) {
        try {
            return ResponseEntity.ok(authenticationUserDetailsService.register(authRegister));
        } catch (Exception e) {
            log.info(e.toString());
            return ResponseEntity.badRequest().build();
        }
    }
}
