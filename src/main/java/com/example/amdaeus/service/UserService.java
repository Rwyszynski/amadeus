package com.example.amdaeus.service;

import com.example.amdaeus.dto.CreateUserRequestDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserDto createUser(CreateUserRequestDto request) {

        User user = new User(
                request.firstName(),
                request.lastName(),
                request.emailAddress(),
                request.userName(),
                request.password(),
                request.userType()
        );

        userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    public User updateRoles(Long id, Set<Role> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserType(roles);
        return userRepository.save(user);
    }

    public UserDto updateUserData(Long id, String firstName, String lastName, String userName, Set<Role> userType) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(userName);
        user.setUserType(userType);
        userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }
}
