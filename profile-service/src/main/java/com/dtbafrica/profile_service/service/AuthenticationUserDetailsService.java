package com.dtbafrica.profile_service.service;

import com.dtbafrica.profile_service.dto.AuthLogin;
import com.dtbafrica.profile_service.dto.AuthLoginResponse;
import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.RefreshToken;
import com.dtbafrica.profile_service.model.UserInfo;
import com.dtbafrica.profile_service.repository.AuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Set;


@Service
@Slf4j
public class AuthenticationUserDetailsService implements UserDetailsService, AuthenticationService {
    
    @Autowired
    AuthenticationRepository authenticationRepository;
    
    @Autowired
    RefreshTokenService refreshTokenService;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;
    
    @Autowired
    JwtService jwtService;
    
    public UserDetails loadUserByUsername(String searchTerm) {
        return authenticationRepository.findByEmail(searchTerm).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    
    @Override
    public UserInfo register(AuthRegister authRegister) {
        authenticationRepository.findByEmail(authRegister.getEmail()).ifPresent(userInfo -> {
            throw new UsernameNotFoundException("User already existed");
        });
        
        UserInfo userInfo = UserInfo.builder()
                .email(authRegister.getEmail())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .name(authRegister.getName())
                .phoneNumber(authRegister.getPhoneNumber())
                .roles(Set.of("ROLE_CUSTOMER")).build();
        return authenticationRepository.save(userInfo);
    }
    
    @Override
    public AuthLoginResponse authenticate(AuthLogin authLogin) {
        UserInfo userDetails = (UserInfo) loadUserByUsername(authLogin.getEmail());
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLogin.getEmail(),
                        authLogin.getPassword()));
        
        if (authentication.isAuthenticated()) {
            return generateAuthResponse(userDetails);
        } else {
            throw new UsernameNotFoundException("User not found or doesn't exist");
        }
    }
    
    public AuthLoginResponse generateAuthResponse(UserInfo userDetails) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getEmail());
        return AuthLoginResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .expiration(refreshToken.getExpiration().toString())
                .accessToken(jwtService.generateToken(userDetails.getUsername(),
                        Date.from(Instant.now().plusMillis(1000 * 60 * 60 * 24))))
                .build();
        
    }
    
    @Override
    @Transactional
    public UserInfo updateUser(Long id, UserInfo user) {
        UserInfo userInfo =
                authenticationRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userInfo.setName(user.getName());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        return userInfo;
    }
    
    @Override
    public UserInfo getUser(Long id) {
        return authenticationRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not foundd"));
    }
    
}