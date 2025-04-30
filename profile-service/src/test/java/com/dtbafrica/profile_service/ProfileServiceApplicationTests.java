package com.dtbafrica.profile_service;

import com.dtbafrica.profile_service.dto.AuthLogin;
import com.dtbafrica.profile_service.dto.AuthLoginResponse;
import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.RefreshToken;
import com.dtbafrica.profile_service.model.UserInfo;
import com.dtbafrica.profile_service.repository.AuthenticationRepository;
import com.dtbafrica.profile_service.service.AuthenticationUserDetailsService;
import com.dtbafrica.profile_service.service.JwtService;
import com.dtbafrica.profile_service.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceApplicationTests {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationRepository authenticationRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtService jwtTokenProvider;
    
    @InjectMocks
    private AuthenticationUserDetailsService authenticationUserDetailsService;
    
    @Test
    void registerUser_Success() {
        UserInfo user = new UserInfo();
        user.setName("testuser");
        user.setPassword("password");
        user.setPhoneNumber("123456789098");
        user.setEmail("test@example.com");
        
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authenticationRepository.save(any(UserInfo.class))).thenReturn(user);
        
        AuthRegister authRegister = AuthRegister.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .build();
        UserInfo result = authenticationUserDetailsService.register(authRegister);
        assertNotNull(result);
        assertEquals("testuser", result.getName());
        verify(authenticationRepository).save(any(UserInfo.class));
    }
    
    @Test
    void authenticate_Success() {
        String email = "testuser@gmail.com";
        String password = "password";
        String name = "testuser";
        String phoneNumber = "123456789012";
        UserInfo user = UserInfo.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .password("encodedPassword")
                .roles(Set.of("ROLE_CUSTOMER"))
                .build();
        
        when(authenticationRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Authentication authResult = new UsernamePasswordAuthenticationToken(
                user,
                password,
                user.getAuthorities());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authResult);
        RefreshToken mockRefreshToken = RefreshToken.builder()
                .refreshToken("refreshToken")
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .userInfo(user)
                .build();
        when(refreshTokenService.createRefreshToken(email)).thenReturn(mockRefreshToken);
        when(jwtTokenProvider.generateToken(eq(email), any(Date.class))).thenReturn("token");
        AuthLoginResponse response = authenticationUserDetailsService.authenticate(
                AuthLogin.builder()
                        .email(email)
                        .password(password)
                        .build());
        
        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertNotNull(response.getExpiration());
        
        verify(authenticationRepository).findByEmail(email);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(eq(email), any(Date.class));
        verify(refreshTokenService).createRefreshToken(email);
    }
    
    @Test
    void authenticate_InvalidCredentials_ThrowsException() {
        when(authenticationRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserInfo()));
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        
        assertThrows(BadCredentialsException.class, () -> {
            authenticationUserDetailsService.authenticate(
                    AuthLogin.builder()
                            .email("wrongemailentered@example.com")
                            .password("wrong")
                            .build());
        });
    }
}