package com.example.amdaeus.service;

import com.example.amdaeus.dto.LoginRequestDto;
import com.example.amdaeus.dto.RegisterUserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.entity.errors.InvalidCredentialsException;
import com.example.amdaeus.entity.errors.UserAlreadyExistsException;
import com.example.amdaeus.entity.errors.UserNotFoundExeption;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@Log4j2
public class AuthorisationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthorisationService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                JwtService jwtService,
                                UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterUserDto dto) {
        if (userRepository.findByEmailAddress(dto.emailAddress()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + dto.emailAddress() + " already exists");
        }

        User user = new User(
                dto.firstName(),
                dto.lastName(),
                dto.emailAddress(),
                dto.userName(),
                passwordEncoder.encode(dto.password()),
                Set.of(Role.STANDARD_ROLE)
        );
        return userRepository.save(user);
    }

    public String login(LoginRequestDto dto) {
        User user = userRepository.findByEmailAddress(dto.email())
                .orElseThrow(() -> new UserNotFoundExeption("User with email " + dto.email() + " not found"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials for email " + dto.email());
        }
        return jwtService.generateToken(user);
    }
}
