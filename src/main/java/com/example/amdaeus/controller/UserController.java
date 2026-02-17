package com.example.amdaeus.controller;

import com.example.amdaeus.dto.CreateUserRequestDto;
import com.example.amdaeus.dto.UpdateUserRequestDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.entity.errors.UserNotFoundExeption;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundExeption("User not found with id: " + id));
        return ResponseEntity.ok(UserMapper.mapToUserDto(user));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserDto> addRoles(@PathVariable("id") Long id,
                                            @RequestBody Set<Role> roles) {
        User user = userService.addRoles(id, roles);
        return ResponseEntity.ok(UserMapper.mapToUserDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequestDto request) {
        UserDto updated = userService.updateUserData(
                id,
                request.firstName(),
                request.lastName(),
                request.userName(),
                request.userType()
        );
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequestDto dto) {
        UserDto created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}

