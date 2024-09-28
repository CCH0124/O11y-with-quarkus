package org.cch.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.cch.entity.ERole;
import org.cch.request.RegisterRequest;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotNull
        String username,
        @Email
        String email,
        @NotNull
        String password,
        LocalDate birthDate,
        @Size(min = 1)
        Set<ERole> roles) {
    public RegisterDTO(RegisterRequest requestVO) {
        this(requestVO.username(), requestVO.email(), requestVO.password(), requestVO.birthDate(),
                requestVO.roles().stream()
                        .map(ERole::valueOf)
                        .collect(Collectors.toSet()));
    }
}
