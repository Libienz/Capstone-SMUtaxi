package com.capstone.smutaxi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;
    Role(String roleName) {
        this.roleName = roleName;
    }
}
