package org.cch.jwt.utils;

import java.io.BufferedReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

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

	public String generateToken(JwtClaimsBuilder jwtClaimsBuilder)
			throws Exception {
				
		PrivateKey pk = readPrivateKey(tokenConfig.privateKeyPath());
		return generateToken(pk, jwtClaimsBuilder);
	}

	public String generateToken(PrivateKey privateKey, JwtClaimsBuilder jwtClaimsBuilder) {

		var currentTime = currentTimeInSecs();
		return jwtClaimsBuilder
				.issuedAt(currentTime)
				.claim(Claims.auth_time.name(), currentTime)
				.expiresAt(currentTime + tokenConfig.expireMilliseconds())
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
	public PrivateKey readPrivateKey(final String pemResName) throws Exception {
		StringWriter sw = new StringWriter();
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(pemResName))) {
			String line = reader.readLine();
			while (line != null) {
				sw.write(line);
				sw.write('\n');
				line = reader.readLine();
			}
		}
		return decodePrivateKey(sw.toString());
	}

	/**
	 * Decode a PEM encoded private key string to an RSA PrivateKey
	 *
	 * @param pemEncoded - PEM string for private key
	 * @return PrivateKey
	 * @throws Exception on decode failure
	 */
	public PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
		byte[] encodedBytes = toEncodedBytes(pemEncoded);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	private byte[] toEncodedBytes(final String pemEncoded) {
		final String normalizedPem = removeBeginEnd(pemEncoded);
		return Base64.getDecoder().decode(normalizedPem);
	}

	private String removeBeginEnd(String pem) {
		pem = pem.replaceAll("-----BEGIN (.*)-----", "");
		pem = pem.replaceAll("-----END (.*)----", "");
		pem = pem.replaceAll("\r\n", "");
		pem = pem.replaceAll("\n", "");
		return pem.trim();
	}

	/**
	 * @return the current time in seconds since epoch
	 */
	public long currentTimeInSecs() {
		return System.currentTimeMillis();
	}

}
