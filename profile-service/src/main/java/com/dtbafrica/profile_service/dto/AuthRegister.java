package com.dtbafrica.profile_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegister {
    @NonNull
    @Email
    @NotBlank
    private String email;
    
    @NonNull
    @NotBlank
    private String password;
    
    @NonNull
    @NotBlank
    private String name;
    
    @NonNull
    @NotBlank
    private String phoneNumber;
    
}
