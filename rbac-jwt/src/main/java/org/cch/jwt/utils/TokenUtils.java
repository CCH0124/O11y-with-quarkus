package org.cch.jwt.utils;
import org.cch.config.Token;
import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
@Singleton
public class TokenUtils {

	@Inject
	Token tokenConfig;

	private TokenUtils() {
	}

	public String generateToken(JwtClaimsBuilder jwtClaimsBuilder) {

		var currentTime = currentTimeInSecs();
		return jwtClaimsBuilder
				.issuedAt(currentTime)
				.claim(Claims.auth_time.name(), currentTime)
				.jws()
				.algorithm(SignatureAlgorithm.RS256)
				.header("typ", "JWT")
				.sign();
	}


	/**
	 * @return the current time in seconds since epoch
	 */
	public long currentTimeInSecs() {
		return System.currentTimeMillis();
	}

}
