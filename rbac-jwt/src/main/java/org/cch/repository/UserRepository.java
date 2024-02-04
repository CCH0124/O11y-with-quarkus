package org.cch.repository;

import java.util.Optional;

import org.cch.entity.User;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> {
    Optional<User> findByUsername(String username) {
        return find("username", username).firstResult();
    }

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
