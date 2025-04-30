package com.dtbafrica.profile_service.service;

import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.UserInfo;
import com.dtbafrica.profile_service.repository.AuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;


@Service
@Slf4j
public class AuthenticationUserDetailsService implements UserDetailsService, AuthenticationService {
    
    @Autowired
    private AuthenticationRepository authenticationRepository;
    
    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;
    
    public UserDetails loadUserByUsername(String searchTerm) {
        return authenticationRepository.findByEmail(searchTerm).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    
    @Override
    public UserInfo register(AuthRegister authRegister) {
        authenticationRepository.findByEmail(authRegister.getEmail()).ifPresent(userInfo -> {
            throw new UsernameNotFoundException("User already exist");
        });
        
        UserInfo userInfo = UserInfo.builder()
                .email(authRegister.getEmail())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .name(authRegister.getName())
                .phoneNumber(authRegister.getPhoneNumber())
                .roles(new HashSet<>()).build();
        return authenticationRepository.save(userInfo);
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
}