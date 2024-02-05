package org.cch.jwt.utils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.inject.Inject;

public class TokenUtils {
	
	@Inject
	static Logger log;

	@ConfigProperty(name = "security.jwt.token.expire-length", defaultValue = "3600")
    private static long validityInMilliseconds;

	private TokenUtils() {
	}

	public static String generateToken(JwtClaimsBuilder jwtClaimsBuilder)
            throws Exception {
        PrivateKey pk = readPrivateKey("/private.pem");
        log.debug("Get Private Key");
        return generateToken(pk, jwtClaimsBuilder);
    }

    public static String generateToken(PrivateKey privateKey, JwtClaimsBuilder jwtClaimsBuilder) {
        
        var currentTime = currentTimeInSecs();
        return jwtClaimsBuilder
			.issuedAt(currentTime)
			.claim(Claims.auth_time.name(), currentTime)
			.expiresAt(currentTime + validityInMilliseconds)
			.jws()
			.algorithm(SignatureAlgorithm.RS256)
			.header("typ", "JWT")
			.sign(privateKey);
    }

	/**
	 * Read a PEM encoded private key from the classpath
	 *
	 * @param pemResName - key file resource name
	 * @return PrivateKey
	 * @throws Exception on decode failure
	 */
	public static PrivateKey readPrivateKey(final String pemResName) throws Exception {
		InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName);
		byte[] tmp = new byte[4096];
		int length = contentIS.read(tmp);
		return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
	}

	/**
	 * Decode a PEM encoded private key string to an RSA PrivateKey
	 *
	 * @param pemEncoded - PEM string for private key
	 * @return PrivateKey
	 * @throws Exception on decode failure
	 */
	public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
		byte[] encodedBytes = toEncodedBytes(pemEncoded);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	private static byte[] toEncodedBytes(final String pemEncoded) {
		final String normalizedPem = removeBeginEnd(pemEncoded);
		return Base64.getDecoder().decode(normalizedPem);
	}

	private static String removeBeginEnd(String pem) {
		pem = pem.replaceAll("-----BEGIN (.*)-----", "");
		pem = pem.replaceAll("-----END (.*)----", "");
		pem = pem.replaceAll("\r\n", "");
		pem = pem.replaceAll("\n", "");
		return pem.trim();
	}

	/**
	 * @return the current time in seconds since epoch
	 */
	public static long currentTimeInSecs() {
		return System.currentTimeMillis();
	}

}
