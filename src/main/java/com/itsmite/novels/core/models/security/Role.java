package com.itsmite.novels.core.models.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 *
 * @author Ehab Arman
 */
@Setter
@Getter
@Document
public class Role {

    @Id
    @Indexed(unique = true, direction = IndexDirection.ASCENDING, dropDups = true)
    private String id;

    private ERole role;

    public Role(ERole role) {
        this.role = role;
    }
}
