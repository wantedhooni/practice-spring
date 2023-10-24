package com.revy.springjwt.service.impl;

import com.revy.springjwt.service.AuthenticationService;
import com.revy.springjwt.service.dto.AuthenticationParamDto;
import com.revy.springjwt.service.dto.AuthenticationResultDto;
import com.revy.springjwt.service.dto.RegisterParamDto;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public AuthenticationResultDto register(RegisterParamDto request) {
        return null;
    }

    @Override
    public AuthenticationResultDto authenticate(AuthenticationParamDto request) {
        return null;
    }
}
