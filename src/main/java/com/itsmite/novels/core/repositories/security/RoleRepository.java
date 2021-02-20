package com.itsmite.novels.core.repositories.security;

import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRole(ERole role);

    boolean existsByRole(ERole role);
}
