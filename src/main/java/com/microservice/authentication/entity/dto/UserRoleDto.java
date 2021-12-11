package com.microservice.authentication.entity.dto;

import com.microservice.authentication.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    private String username;

    private RoleEnum role;
}
