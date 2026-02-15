package com.example.amdaeus.dto;

import com.example.amdaeus.entity.Role;

import java.util.Set;

public record UserDto(String firstName, String lastName, String emailAddress, String userName, Set<Role> userType) {}

