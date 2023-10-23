package com.revy.springjwt.enums;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(
            Set.of(Privilege.READ_PRIVILEGE, Privilege.WRITE_PRIVILEGE, Privilege.UPDATE_PRIVILEGE, Privilege.DELETE_PRIVILEGE)
    ),
    USER(
            Set.of(Privilege.READ_PRIVILEGE, Privilege.WRITE_PRIVILEGE)
    ),
    ;

    @Getter
    private final Set<Privilege> privileges;

    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPrivileges()
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
