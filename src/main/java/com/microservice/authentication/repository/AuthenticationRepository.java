package com.microservice.authentication.repository;

import com.microservice.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
