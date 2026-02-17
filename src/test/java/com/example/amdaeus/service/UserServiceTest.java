package com.example.amdaeus.service;

import com.example.amdaeus.dto.CreateUserRequestDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should return all users")
    void getAllUsersTest() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("should return user by id")
    void getUserByIdTest() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("should throw exception if user not found")
    void getUserByIdNotFoundTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateRoles(1L, Set.of(Role.ADMIN_ROLE)));
    }

    @Test
    @DisplayName("should update roles")
    void updateRolesTest() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        Set<Role> roles = Set.of(Role.STANDARD_ROLE);
        User result = userService.updateRoles(1L, roles);

        assertThat(result.getUserType()).contains(Role.STANDARD_ROLE);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("should add roles to user")
    void addRolesTest() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addRoles(1L, Set.of(Role.ADMIN_ROLE));

        assertThat(result.getUserType()).contains(Role.ADMIN_ROLE);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("should update user data")
    void updateUserDataTest() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUserData(1L, "John", "Doe", "johndoe", Set.of(Role.STANDARD_ROLE));

        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.userName()).isEqualTo("johndoe");
        assertThat(user.getUserType()).contains(Role.STANDARD_ROLE);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("should find user by username")
    void findByUserNameTest() {
        User user = new User();
        when(userRepository.findByUserName("john")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUserName("john");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("should get currently authenticated user")
    void getCurrentAuthenticatedUserTest() {
        // Mock user i repository
        User user = new User();
        user.setEmailAddress("john.doe@test.com");
        when(userRepository.findByEmailAddress("john.doe@test.com")).thenReturn(Optional.of(user));

        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john.doe@test.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        // Mock SecurityContext i podmiana w SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Wywołanie metody
        User result = userService.getCurrentAuthenticatedUser();

        assertThat(result).isEqualTo(user);
    }


    @Test
    @DisplayName("should create new user")
    void createUserTest() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "John", "Doe", "john.doe@test.com", "johndoe", "password", Set.of(Role.STANDARD_ROLE)
        );

        UserDto result = userService.createUser(dto);

        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.userName()).isEqualTo("johndoe");
        verify(userRepository).save(any(User.class));
    }
}
