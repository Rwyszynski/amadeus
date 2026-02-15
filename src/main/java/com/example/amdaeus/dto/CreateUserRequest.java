package com.example.amdaeus.dto;

import com.example.amdaeus.entity.Role;

import java.util.Set;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String emailAddress,
        String userName,
        String password,
        Set<Role> userType
) {}
