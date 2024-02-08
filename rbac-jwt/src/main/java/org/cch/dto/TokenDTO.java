package org.cch.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import org.cch.entity.ERole;

public record TokenDTO(
        String userId,
        String username,
        String email,
        LocalDate birthDate,
        Set<ERole> roles) {
    public TokenDTO(String userId,
            String username,
            String email,
            LocalDate birthDate,
            Set<ERole> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
        this.roles = roles;
        
    }
}
