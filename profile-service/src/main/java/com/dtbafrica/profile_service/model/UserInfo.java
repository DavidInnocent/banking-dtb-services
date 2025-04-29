package com.dtbafrica.profile_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Builder
@Table(name = "user_info")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Size(min = 5, max = 60)
    private String name;
    
    @Email
    @NotNull(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotNull
    private List<RoleType> roles;
    
    @NotNull(message = "Mobile number is required")
    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "\\d{12}", message = "Mobile number must be 12 digits")
    private String phoneNumber;
    
    @Size(min = 8, message = "Password should be minimun 8 characters")
    private String password;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
