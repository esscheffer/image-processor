package com.bix.image_processor.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    SIMPLE("SIMPLE"), PREMIUM("PREMIUM");

    private final String role;
}
