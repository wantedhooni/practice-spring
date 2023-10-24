package com.revy.springjwt.service.impl;

import com.revy.springjwt.domain.RefreshToken;
import com.revy.springjwt.domain.User;
import com.revy.springjwt.domain.UserRepo;
import com.revy.springjwt.enums.Role;
import com.revy.springjwt.enums.TokenType;
import com.revy.springjwt.service.AuthenticationService;
import com.revy.springjwt.service.JwtService;
import com.revy.springjwt.service.RefreshTokenService;
import com.revy.springjwt.service.dto.AuthenticationParamDto;
import com.revy.springjwt.service.dto.AuthenticationResultDto;
import com.revy.springjwt.service.dto.RegisterParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResultDto register(RegisterParamDto request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepo.save(user);
        String jwt = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        List<String> roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResultDto.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthenticationResultDto authenticate(AuthenticationParamDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        List<String> roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        String jwt = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResultDto.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();

    }
}
