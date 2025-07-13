package org.cch.entity;

import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
@Cacheable
@Schema(name = "Role", description = "Entity that represents a Role.")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "UUID")
    @Column(columnDefinition = "uuid DEFAULT uuid_generate_v4()")
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "erole")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ERole name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

}
