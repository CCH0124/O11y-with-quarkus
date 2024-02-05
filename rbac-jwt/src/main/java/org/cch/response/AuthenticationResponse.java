package org.cch.response;

public record AuthenticationResponse(String token) {
    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
