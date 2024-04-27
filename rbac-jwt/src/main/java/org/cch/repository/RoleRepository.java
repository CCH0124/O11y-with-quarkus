package org.cch.repository;

import java.util.Optional;

import org.cch.entity.ERole;
import org.cch.entity.Role;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<Role, String> {
    
    public Optional<Role> findByNameOptional(ERole name) {
        return find("name = :name", Parameters.with("name", name)).firstResultOptional();
    }

}
