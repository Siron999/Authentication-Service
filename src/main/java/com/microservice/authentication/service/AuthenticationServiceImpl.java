package com.microservice.authentication.service;

import com.microservice.authentication.entity.Role;
import com.microservice.authentication.entity.RoleEnum;
import com.microservice.authentication.entity.User;
import com.microservice.authentication.entity.dto.CurrentUserDto;
import com.microservice.authentication.entity.dto.LoginDto;
import com.microservice.authentication.exception.RecordAlreadyExistsException;
import com.microservice.authentication.exception.RecordNotFoundException;
import com.microservice.authentication.repository.AuthenticationRepository;
import com.microservice.authentication.repository.RoleRepository;
import com.microservice.authentication.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements UserDetailsService, AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadByUsername");
        Optional<User> user = authenticationRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User {} found in database", username);
        } else {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.get().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole().toString()));
        });
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
    }

    @Override
    public CurrentUserDto register(User user) throws RecordAlreadyExistsException {
        log.info("Inside register");
        Optional<User> user1 = authenticationRepository.findByUsername(user.getUsername());
        if (user1.isEmpty()) {
            log.info("User does not exist yet");
        } else {
            log.error("User already Exists");
            throw new RecordAlreadyExistsException("User already exists");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User savedUser = authenticationRepository.save(user);
        return login(new LoginDto(savedUser.getUsername(), savedUser.getPassword()));
    }

    @Override
    public CurrentUserDto getCurrentUserDto(String username, String token) throws UsernameNotFoundException {
        log.info("Inside getCurrentUserDto");
        Optional<User> user = authenticationRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User {} found in database", username);
        } else {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CurrentUserDto(user.get().getUsername(), user.get().getEmail(), user.get().getRoles(), user.get().getAddress(), token);
    }

    @Override
    public CurrentUserDto getCurrentUser(String username) throws UsernameNotFoundException {
        log.info("Inside getCurrentUser : {}",username);
        Optional<User> user = authenticationRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User {} found in database", username);
        } else {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CurrentUserDto(user.get().getUsername(), user.get().getEmail(), user.get().getRoles(), user.get().getAddress(), null);
    }

    @Override
    public Role saveRole(Role role) throws RecordAlreadyExistsException {
        log.info("Inside saveRole");
        Optional<Role> role1 = roleRepository.findByRole(role.getRole());
        if (role1.isEmpty()) {
            log.info("Role does not exist yet");
        } else {
            log.error("User already Exists");
            throw new RecordAlreadyExistsException("Role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public String addRoleToUser(String username, RoleEnum roleName) throws RecordNotFoundException, RecordAlreadyExistsException {
        log.info("Inside addRoleToUser");
        Optional<User> user = authenticationRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User {} found in database", username);
        } else {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        Optional<Role> role = roleRepository.findByRole(roleName);

        if (role.isPresent()) {
            log.info("Role {} found in database", roleName);
        } else {
            log.error("Role Not Found");
            throw new RecordNotFoundException("Role not found");
        }

        if (user.get().getRoles().contains(role.get())) {
            log.error("Role is already assigned to the user");
            throw new RecordAlreadyExistsException("Role is already assigned to the user");
        }
        user.get().getRoles().add(role.get());
        return "Role added to user successfully";
    }

    @Override
    public CurrentUserDto login(LoginDto loginDto) {
        log.info("Inside login");
        UserDetails userDetails = loadUserByUsername(loginDto.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return getCurrentUserDto(loginDto.getUsername(), token);
    }
}
