package com.example.amdaeus.service;

import com.example.amdaeus.dto.CreateUserRequestDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
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

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        String username = authentication.getName();
        return userRepository.findByEmailAddress(username)
                .orElseThrow(() -> new RuntimeException("Did not found user " + username));
    }

    public User addRoles(Long id, Set<Role> newRoles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserType() == null) {
            user.setUserType(new HashSet<>());
        }
        user.getUserType().addAll(newRoles);
        return userRepository.save(user);
    }

    public UserDto createUser(CreateUserRequestDto dto) {
        User user = new User(
                dto.firstName(),
                dto.lastName(),
                dto.emailAddress(),
                dto.userName(),
                dto.password(),
                dto.userType()
        );
        userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }
}
