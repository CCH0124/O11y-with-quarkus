package org.cch.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.cch.dto.TokenDTO;
import org.cch.jwt.utils.TokenUtils;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;


import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class TokenService {
    
    @Inject
    Logger log;
    
    private static final String ISSUER = "https://localhost.com/issuer";

    public String generateToken(TokenDTO tokenDTO) {
        Set<String> groups = tokenDTO.roles().stream().map(x -> x.name()).collect(Collectors.toSet());

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        claimsBuilder
            .issuer(ISSUER)
            .subject(tokenDTO.email())
            .claim(Claims.upn.name(), tokenDTO.email())
            .claim(Claims.preferred_username.name(), tokenDTO.username())
            .claim(Claims.email.name(), tokenDTO.email())
            .claim("id", tokenDTO.userId())
            .claim(Claims.birthdate.name(), tokenDTO.birthDate())
            .groups(groups);

        try {
            return TokenUtils.generateToken(claimsBuilder);
        } catch (Exception e) {
            log.error("Token generate faild.");
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
