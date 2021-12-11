package com.microservice.authentication.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.microservice.authentication.entity.Address;
import com.microservice.authentication.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentUserDto {

    private String username;

    private String email;

    private List<Role> roles;

    private Address address;

    private String access_token;
}
