package com.itsmite.novels.core.services.security;

import com.itsmite.novels.core.errors.exceptions.AlreadyUsedException;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.repositories.security.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;

@Slf4j
@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void autowireBeans(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(ERole eRole) {
        if (roleRepository.existsByRole(eRole)) {
            throw new AlreadyUsedException("role", eRole.name());
        }
        Role role = new Role(eRole);
        try {
            return roleRepository.save(role);
        } catch (Exception ex) {
            log.error("Failed to create role: Something went wrong: {}", ex.getMessage());
            throw new InternalServerErrorException("Something went wrong");
        }
    }
}
