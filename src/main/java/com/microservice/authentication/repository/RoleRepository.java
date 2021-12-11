package com.microservice.authentication.repository;

import com.microservice.authentication.entity.Role;
import com.microservice.authentication.entity.RoleEnum;
import com.microservice.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByRole(RoleEnum roleName);
}
