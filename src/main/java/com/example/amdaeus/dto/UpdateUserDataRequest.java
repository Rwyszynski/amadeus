package com.example.amdaeus.dto;

public record UpdateUserDataRequest(
        String firstName,
        String lastName,
        String emailAddress
) {}
