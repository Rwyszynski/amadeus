package com.example.amdaeus.dto;

import com.example.amdaeus.entity.Role;

import java.util.Set;

public record UpdateUserRolesRequest(
        Set<Role> roles
) {}
