package com.dtbafrica.profile_service.service;


import com.dtbafrica.profile_service.dto.AuthRegister;
import com.dtbafrica.profile_service.model.UserInfo;

import java.util.List;

public interface AuthenticationService {
    UserInfo register(AuthRegister authRegister) throws Exception;
}
