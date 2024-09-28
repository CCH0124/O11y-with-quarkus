package org.cch.request;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;


public record RegisterRequest(
        String username,
        String email,
        String password,
        @JsonProperty("birth_date")
        LocalDate birthDate,
        Set<String> roles) {

    public RegisterRequest(String username, String email, String password, LocalDate birthDate, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.roles = roles;
    }

}
