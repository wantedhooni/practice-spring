package com.revy.springjwt.service.impl;

import com.revy.springjwt.domain.RefreshToken;
import com.revy.springjwt.domain.RefreshTokenRepo;
import com.revy.springjwt.domain.User;
import com.revy.springjwt.domain.UserRepo;
import com.revy.springjwt.enums.TokenType;
import com.revy.springjwt.service.JwtService;
import com.revy.springjwt.service.RefreshTokenService;
import com.revy.springjwt.service.dto.RefreshTokenParamDto;
import com.revy.springjwt.service.dto.RefreshTokenResultDto;
import com.revy.springjwt.service.exception.TokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        log.info("[createRefreshToken] param userId:{}", userId);
        Assert.notNull(userId, "userId must be not null.");

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(this.refreshExpiration))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        log.info("[verifyExpiration] param token:{}", refreshToken);
        Assert.notNull(refreshToken, "refreshToken must be not null.");

        if (refreshToken.getExpiryDate().isAfter(Instant.now())) {
            refreshTokenRepo.delete(refreshToken);
            throw new TokenException(refreshToken.getToken(), "토큰이 만료 되었습니다. 다시 인증해 주세요.");
        }
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        log.info("[findByToken] param token:{}", token);
        Assert.hasText(token, "token must be not empty.");
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public RefreshTokenResultDto generateNewToken(RefreshTokenParamDto refreshTokenParamDto) {
        Assert.notNull(refreshTokenParamDto, "refreshTokenParamDto must not be null.");
        User user = refreshTokenRepo.findByToken(refreshTokenParamDto.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenException(refreshTokenParamDto.getRefreshToken(), "RefreshToken이 존재하지 않습니다."));

        String token = jwtService.generateToken(user);
        return RefreshTokenResultDto.builder()
                .accessToken(token)
                .refreshToken(refreshTokenParamDto.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        log.info("[generateRefreshTokenCookie] param token:{}", token);
        Assert.hasText(token, "token must not be empty.");

        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration / 1000)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        return cookie == null ? "" : cookie.getValue();
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepo.findByToken(token).ifPresent(refreshTokenRepo::delete);
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .build();
    }
}
