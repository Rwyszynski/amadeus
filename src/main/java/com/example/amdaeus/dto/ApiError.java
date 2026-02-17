package com.example.amdaeus.dto;

public record ApiError(
        int status,
        String error,
        String message
) {}