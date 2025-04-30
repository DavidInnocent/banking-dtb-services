package com.dtbafrica.profile_service.service;


import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.UserInfo;

public interface AuthenticationService {
    UserInfo register(AuthRegister authRegister);
    
    UserInfo updateUser(Long id, UserInfo user);
}
