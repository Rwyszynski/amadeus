package com.example.amdaeus.mappers;

import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    private UserMapper() {}

    public static UserDto mapToUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmailAddress(),
                user.getUserName(),
                user.getUserType()
        );
    }
}
