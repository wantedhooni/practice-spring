package com.revy.springjwt.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class AuthenticationParamDto {

    private String email;
    private String password;

    @Builder
    public AuthenticationParamDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
