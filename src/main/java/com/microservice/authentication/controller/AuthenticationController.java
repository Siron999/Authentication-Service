package com.microservice.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.authentication.entity.Role;
import com.microservice.authentication.entity.User;
import com.microservice.authentication.entity.dto.CurrentUserDto;
import com.microservice.authentication.entity.dto.LoginDto;
import com.microservice.authentication.entity.dto.UserRoleDto;
import com.microservice.authentication.exception.RecordAlreadyExistsException;
import com.microservice.authentication.exception.RecordNotFoundException;
import com.microservice.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<CurrentUserDto> register(@Valid @RequestBody User user) throws RecordAlreadyExistsException {
        log.info("Inside register");
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(user));
    }

    @PostMapping("login")
    public ResponseEntity<CurrentUserDto> login(@Valid @RequestBody LoginDto loginDto) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        return ResponseEntity.ok().body(authenticationService.login(loginDto));
    }

    @PostMapping("role/save")
    public ResponseEntity<Role> saveRole(@Valid @RequestBody Role role) throws RecordAlreadyExistsException {
        log.info("Inside saveRole");
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.saveRole(role));
    }

    @PostMapping("role/add")
    public ResponseEntity<String> addRoleToUser(@Valid @RequestBody UserRoleDto userRoleDto) throws RecordNotFoundException, RecordAlreadyExistsException {
        log.info("Inside addRoleToUser");
        return ResponseEntity.ok().body(authenticationService.addRoleToUser(userRoleDto.getUsername(), userRoleDto.getRole()));
    }

    @GetMapping("current-user")
    public ResponseEntity<CurrentUserDto> currentUser(Authentication authentication) throws JsonProcessingException {
        log.info("Inside currentUser : {}", authentication.getName());
        return ResponseEntity.ok().body(authenticationService.getCurrentUser(authentication.getName()));
    }
}
