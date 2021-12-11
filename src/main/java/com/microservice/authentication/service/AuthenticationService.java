package com.microservice.authentication.service;

import com.microservice.authentication.entity.Role;
import com.microservice.authentication.entity.RoleEnum;
import com.microservice.authentication.entity.User;
import com.microservice.authentication.entity.dto.CurrentUserDto;
import com.microservice.authentication.entity.dto.LoginDto;
import com.microservice.authentication.exception.RecordAlreadyExistsException;
import com.microservice.authentication.exception.RecordNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthenticationService {
    CurrentUserDto register(User user) throws RecordAlreadyExistsException;

    CurrentUserDto getCurrentUserDto(String username, String token) throws UsernameNotFoundException;

    CurrentUserDto getCurrentUser(String username) throws UsernameNotFoundException;

    CurrentUserDto login(LoginDto loginDto);

    Role saveRole(Role role) throws RecordAlreadyExistsException;

    String addRoleToUser(String username, RoleEnum roleName) throws RecordNotFoundException, RecordAlreadyExistsException;
}
