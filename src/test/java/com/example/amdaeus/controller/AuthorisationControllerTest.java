package com.example.amdaeus.controller;

import com.example.amdaeus.dto.LoginRequestDto;
import com.example.amdaeus.dto.RegisterUserDto;
import com.example.amdaeus.dto.RegisterUserResponseDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.service.AuthorisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthorisationControllerTest {

    private AuthorisationService authorisationService;
    private AuthorisationController controller;

    @BeforeEach
    void setUp() {
        authorisationService = mock(AuthorisationService.class);
        controller = new AuthorisationController(authorisationService);
    }

    @Test
    void register_ShouldReturnSuccessMessage() {
        RegisterUserDto dto = new RegisterUserDto("John", "Doe", "john@example.com", "johndoe", "password123");

        // Mock service to return a User
        User mockUser = new User("John", "Doe", "john@example.com", "johndoe", "encodedPass", Set.of(Role.STANDARD_ROLE));
        when(authorisationService.registerUser(dto)).thenReturn(mockUser);

        ResponseEntity<RegisterUserResponseDto> response = controller.register(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("User registered successfully");

        // Verify service was called once
        verify(authorisationService, times(1)).registerUser(dto);
    }

    @Test
    void login_ShouldReturnToken() {
        LoginRequestDto dto = new LoginRequestDto("john@example.com", "password123");

        when(authorisationService.login(dto)).thenReturn("mocked-jwt-token");

        ResponseEntity<String> response = controller.login(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("mocked-jwt-token");

        verify(authorisationService, times(1)).login(dto);
    }

    @Test
    void getMyInfo_ShouldReturnUnauthorized_WhenNoAuthentication() {
        SecurityContext mockContext = mock(SecurityContext.class);
        when(mockContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(mockContext);

        ResponseEntity<?> response = controller.getMyInfo();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Unauthorized: No authenticated user found");
    }

    @Test
    void getMyInfo_ShouldReturnUnauthorized_WhenAnonymous() {
        SecurityContext mockContext = mock(SecurityContext.class);
        Authentication anonymous = mock(AnonymousAuthenticationToken.class);
        when(mockContext.getAuthentication()).thenReturn(anonymous);
        SecurityContextHolder.setContext(mockContext);

        ResponseEntity<?> response = controller.getMyInfo();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Unauthorized: No authenticated user found");
    }
/*
    @Test
    void getMyInfo_ShouldReturnUserInfo_WhenAuthenticated() {
        // Mock SecurityContext
        SecurityContext mockContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockContext);

        // Mock UserDetails
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("johndoe");

        // Tworzymy authorities jako Collection<? extends GrantedAuthority>
        GrantedAuthority authority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_STANDARD";
            }
        };
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
        when(userDetails.getAuthorities()).thenReturn(authorities);

        // Mock Authentication
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(userDetails);

        // Ustawiamy mockContext
        when(mockContext.getAuthentication()).thenReturn(auth);

        // Wywołanie metody
        ResponseEntity<?> response = controller.getMyInfo();

        // Assercje
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOfAny(Map.class);

        Map<?, ?> map = (Map<?, ?>) response.getBody();
        assertThat(map.get("username")).isEqualTo("johndoe");
        assertThat(map.get("roles")).isNotNull();
    }
*/
}
