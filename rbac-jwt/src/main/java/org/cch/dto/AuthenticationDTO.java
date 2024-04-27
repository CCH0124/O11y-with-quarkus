package org.cch.dto;

import org.cch.request.AuthenticationRequest;

public record AuthenticationDTO(String username, String password) {
    public AuthenticationDTO(AuthenticationRequest requestVO) {
        this(requestVO.username(), requestVO.password());
    }
}
