package org.cch.dto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.cch.entity.ERole;
import org.cch.request.RegisterRequest;

public record RegisterDTO(
        String username,
        String email,
        String password,
        Instant birthDate,
        Set<ERole> roles) {
    public RegisterDTO(RegisterRequest requestVO) {
        this(requestVO.username(), requestVO.email(), requestVO.password(), requestVO.birthDate(),
                requestVO.roles().stream()
                        .map(ERole::valueOf)
                        .collect(Collectors.toSet()));
    }
}
