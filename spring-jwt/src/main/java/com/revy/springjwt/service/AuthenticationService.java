package com.revy.springjwt.service;

import com.revy.springjwt.service.dto.AuthenticationParamDto;
import com.revy.springjwt.service.dto.AuthenticationResultDto;
import com.revy.springjwt.service.dto.RegisterParamDto;

public interface AuthenticationService {

    AuthenticationResultDto register(RegisterParamDto request);

    AuthenticationResultDto authenticate(AuthenticationParamDto request);
}
