package com.example.amdaeus.dto;

import com.example.amdaeus.entity.Role;

import java.util.List;

public record UserRoleDto(List<Role> roles) {
}
