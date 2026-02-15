package com.example.amdaeus.controller;

import com.example.amdaeus.dto.CreateUserRequestDto;
import com.example.amdaeus.dto.UpdateUserRequestDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequestDto request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserDto> updateRoles(@PathVariable("id") Long id, @RequestBody Set<Role> roles) {

        User user = userService.updateRoles(id, roles);
        return ResponseEntity.ok(UserMapper.mapToUserDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequestDto request
    ) {
        UserDto updated = userService.updateUserData(
                userId,
                request.firstName(),
                request.lastName(),
                request.userName(),
                request.userType()
        );
        return ResponseEntity.ok(updated);
    }
}

