package org.cch.repository;

import java.util.Optional;

import org.cch.entity.User;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> {
    
    public Optional<User> findByUsernameOptional(String username) {
        return find("username", username).firstResultOptional();
    }

    public Boolean existsByUsername(String username) {
        return count("username", username) > 0;
    }

    public Boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
}
