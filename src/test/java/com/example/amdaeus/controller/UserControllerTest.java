package com.example.amdaeus.controller;

import com.example.amdaeus.dto.UpdateUserRequestDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController controller;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        controller = new UserController(userService);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDto() {
        User user1 = new User("John", "Doe", "john@example.com", "johndoe", "pass", Set.of(Role.STANDARD_ROLE));
        user1.setId(1L);
        User user2 = new User("Jane", "Smith", "jane@example.com", "janesmith", "pass", Set.of(Role.APPROVER_ROLE));
        user2.setId(2L);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserDto>> response = controller.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody())
                .extracting(UserDto::userName)
                .containsExactly("johndoe", "janesmith");

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUser_ShouldReturnUserDto() {
        Long userId = 1L;
        User user = new User("John", "Doe", "john@example.com", "johndoe", "pass", Set.of(Role.STANDARD_ROLE));
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<UserDto> response = controller.getUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().userName()).isEqualTo("johndoe");
        assertThat(response.getBody().userType()).containsExactly(Role.STANDARD_ROLE);

        verify(userService, times(1)).getUserById(userId);
    }


    @Test
    void updateUser_ShouldReturnUpdatedUserDto() {
        Long userId = 1L;
        Set<Role> roles = Set.of(Role.STANDARD_ROLE);

        UpdateUserRequestDto request = new UpdateUserRequestDto(
                "John",
                "Doe",
                "johndoe",
                Set.of(Role.STANDARD_ROLE)
        );

        User updatedUser = new User("John", "Doe", "john@example.com", "johndoe", "password123", roles);
        updatedUser.setId(userId);

        // updateUserData w UserService zwraca UserDto
        when(userService.updateUserData(userId, "John", "Doe", "johndoe", roles))
                .thenReturn(UserMapper.mapToUserDto(updatedUser));

        ResponseEntity<UserDto> response = controller.updateUser(userId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().userName()).isEqualTo("johndoe");
        assertThat(response.getBody().userType()).containsExactly(Role.STANDARD_ROLE);

        verify(userService, times(1))
                .updateUserData(userId, "John", "Doe", "johndoe", roles);
    }
}
