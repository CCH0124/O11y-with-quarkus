package org.cch.service;

import org.cch.dto.TokenDTO;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class TokenService {
    public String generateToken(TokenDTO tokenDTO) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setSubject(tokenDTO.birthDate());
        jwtClaims.setClaim(Claims.upn.name(), tokenDTO.email());
        jwtClaims.setClaim(Claims.preferred_username.name(), tokenDTO.username());
        jwtClaims.setClaim(Claims.birthdate.name(), tokenDTO.birthDate());
        // jw
        return "";

    }
}
