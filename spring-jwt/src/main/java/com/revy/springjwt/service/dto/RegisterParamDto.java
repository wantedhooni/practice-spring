package com.revy.springjwt.service.dto;

import com.revy.springjwt.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterParamDto {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
