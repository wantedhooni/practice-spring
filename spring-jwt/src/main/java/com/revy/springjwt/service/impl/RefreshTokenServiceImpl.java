package com.revy.springjwt.service.impl;

import com.revy.springjwt.domain.RefreshToken;
import com.revy.springjwt.service.RefreshTokenService;
import com.revy.springjwt.service.dto.RefreshTokenParamDto;
import com.revy.springjwt.service.dto.RefreshTokenResultDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Override
    public RefreshToken createRefreshToken(Long userId) {
        return null;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        return null;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public RefreshTokenResultDto generateNewToken(RefreshTokenParamDto request) {
        return null;
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return null;
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        return null;
    }

    @Override
    public void deleteByToken(String token) {

    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return null;
    }
}
