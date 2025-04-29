package com.dtbafrica.profile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class ProfileServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }
    
    @Bean
    public BCryptPasswordEncoder provideEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
