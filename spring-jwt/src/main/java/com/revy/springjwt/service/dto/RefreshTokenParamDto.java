package com.revy.springjwt.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RefreshTokenParamDto {
    private String refreshToken;
}
