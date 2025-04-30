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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/user")
@Slf4j
public class AuthenticationController {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    AuthenticationUserDetailsService authenticationUserDetailsService;
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthLogin authLogin) {
        try{
            return ResponseEntity.ok(authenticationUserDetailsService.authenticate(authLogin));
        }catch (Exception e){
            return ResponseEntity.status(401).build();
        }
    }
    
   
    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRegister authRegister) {
        try {
            return ResponseEntity.ok(authenticationUserDetailsService.register(authRegister));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #id == principal.id)")
    public ResponseEntity<UserInfo> updateUser(@PathVariable Long id, @RequestBody UserInfo user) {
        UserInfo updatedUser = authenticationUserDetailsService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUser(@PathVariable Long id) {
        UserInfo updatedUser = authenticationUserDetailsService.getUser(id);
        return ResponseEntity.ok(updatedUser);
    }
    
}
