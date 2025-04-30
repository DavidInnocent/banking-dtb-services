package com.dtbafrica.profile_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthLogin {
    @Email
    @NonNull
    private String email;
    @NonNull
    private String password;
}
