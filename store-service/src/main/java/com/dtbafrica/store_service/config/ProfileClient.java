package com.dtbafrica.store_service.config;

import com.dtbafrica.profile_service.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${profile.service.url}")
public interface ProfileClient {
    @GetMapping("/api/auth/{id}")
    UserInfo getUser(@PathVariable("id") Long id);
}