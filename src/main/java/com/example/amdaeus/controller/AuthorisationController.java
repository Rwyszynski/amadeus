package com.example.amdaeus.controller;

import com.example.amdaeus.dto.LoginRequestDto;
import com.example.amdaeus.dto.RegisterUserDto;
import com.example.amdaeus.dto.RegisterUserResponseDto;
import com.example.amdaeus.service.AuthorisationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/auth")
public class AuthorisationController {

    private final AuthorisationService authorisationService;

    public AuthorisationController(AuthorisationService authorisationService) {
        this.authorisationService = authorisationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(@Valid @RequestBody RegisterUserDto registerUserDto){
        authorisationService.registerUser(registerUserDto);
        return ResponseEntity.ok(new RegisterUserResponseDto("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto dto) {
        String token = authorisationService.login(dto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: No authenticated user found");
        }
        UserDetails user = (UserDetails) auth.getPrincipal();
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("roles", user.getAuthorities());

        return ResponseEntity.ok(response);
    }
}
