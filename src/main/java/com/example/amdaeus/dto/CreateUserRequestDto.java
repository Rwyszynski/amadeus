package com.example.amdaeus.dto;

import com.example.amdaeus.entity.Role;
import java.util.Set;

public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String emailAddress,
        String userName,
        String password,
        Set<Role> userType
) {}