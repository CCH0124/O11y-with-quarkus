package org.cch.service;

import org.cch.dto.TokenDTO;
import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.JwtClaimsBuilder;

public interface TokenService {
    String generateToken(TokenDTO tokenDTO);

    default String generateToken(JwtClaimsBuilder jwtClaimsBuilder) {

		var currentTime = System.currentTimeMillis();
		return jwtClaimsBuilder
				.issuedAt(currentTime)
				.claim(Claims.auth_time.name(), currentTime)
				.jws()
				.algorithm(SignatureAlgorithm.RS256)
				.header("typ", "JWT")
				.sign();
	}
}
