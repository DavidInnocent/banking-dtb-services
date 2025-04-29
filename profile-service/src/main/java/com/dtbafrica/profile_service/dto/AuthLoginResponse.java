package com.dtbafrica.profile_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuthLoginResponse {
    private String refreshToken;
    private String accessToken;
    private String expiration;
}



