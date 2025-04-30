package com.dtbafrica.profile_service.service;


import com.dtbafrica.profile_service.dto.AuthLogin;
import com.dtbafrica.profile_service.dto.AuthLoginResponse;
import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.UserInfo;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    UserInfo register(AuthRegister authRegister);
    AuthLoginResponse authenticate(AuthLogin authLogin);
    
    UserInfo updateUser(Long id, UserInfo user);
    UserInfo getUser(Long id);
}
