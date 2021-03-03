package com.itsmite.novels.core.repositories.user;

import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.repositories.ResourceRepository;

import java.util.Optional;

public interface UserRepository extends ResourceRepository<User, String> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
