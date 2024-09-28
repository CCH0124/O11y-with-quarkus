package org.cch.response;

public record AuthenticationResponse(String token, String expiresIn) {

}
