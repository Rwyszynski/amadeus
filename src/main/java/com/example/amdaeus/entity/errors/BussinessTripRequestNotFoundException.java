package com.example.amdaeus.entity.errors;

public class BussinessTripRequestNotFoundException extends RuntimeException {
    public BussinessTripRequestNotFoundException(String message) {
        super(message);
    }
}
