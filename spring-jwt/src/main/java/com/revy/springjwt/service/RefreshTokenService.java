package com.revy.springjwt.service;

import com.revy.springjwt.domain.RefreshToken;
import com.revy.springjwt.service.dto.RefreshTokenParamDto;
import com.revy.springjwt.service.dto.RefreshTokenResultDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResultDto generateNewToken(RefreshTokenParamDto request);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}
