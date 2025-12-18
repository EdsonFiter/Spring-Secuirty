package com.devedson.security.domain.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
    USER,
    ADMIN,
    MANAGER;
}
