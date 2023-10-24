package com.revy.springjwt.service.dto;

import lombok.*;

import java.util.List;


@Setter
@Getter
@ToString
@NoArgsConstructor
public class AuthenticationResultDto {

    private  Long id;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    @Builder
    public AuthenticationResultDto(Long id, String email, List<String> roles, String accessToken, String refreshToken, String tokenType) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }
}
