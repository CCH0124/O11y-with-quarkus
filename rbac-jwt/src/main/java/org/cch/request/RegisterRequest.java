package org.cch.request;

import java.time.Instant;
import java.util.Set;


public record RegisterRequest(
        String username,
        String email,
        String password,
        Instant birthDate,
        Set<String> roles) {

    public RegisterRequest(String username, String email, String password, Instant birthDate, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.roles = roles;
    }

}
