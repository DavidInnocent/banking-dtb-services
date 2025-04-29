package com.dtbafrica.profile_service.service;


import com.dtbafrica.profile_service.model.RefreshToken;
import com.dtbafrica.profile_service.model.UserInfo;
import com.dtbafrica.profile_service.repository.AuthenticationRepository;
import com.dtbafrica.profile_service.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {
    
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;
    
    /**
     *
     * @param username user for which the token is to be saved
     * @return refresh token response
     */
    @Transactional
    public RefreshToken createRefreshToken(String username) {
        Date expirationTime = Date.from(Instant.now().plusMillis(1000 * 60 * 60 * 24 * 7));
        String refresh = UUID.randomUUID().toString();
        UserInfo userInfo =
                authenticationRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User " +
                        "not found"));
        Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUserInfoId(userInfo.getId());
        RefreshToken toBeSaved = existingRefreshToken.orElseGet(() -> RefreshToken.builder()
                .userInfo(userInfo)
                .refreshToken(refresh)
                .expiration(expirationTime)
                .build());
        toBeSaved.setExpiration(expirationTime);
        toBeSaved.setRefreshToken(refresh);
        return refreshTokenRepository.save(toBeSaved);
    }
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }
    
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiration().before(new Date())) {
            refreshTokenRepository.delete(token);
            return null;
        }
        return token;
    }
}