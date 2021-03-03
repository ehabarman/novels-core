package com.itsmite.novels.core.repositories.security;

import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.repositories.ResourceRepository;

import java.util.Optional;

public interface RoleRepository extends ResourceRepository<Role, String> {

    Optional<Role> findByRole(ERole role);

    boolean existsByRole(ERole role);
}
