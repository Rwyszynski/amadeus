package com.example.amdaeus.mappers;

import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    @DisplayName("should map User to UserDto correctly")
    void shouldMapUserToUserDtoCorrectly() {
        // given
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmailAddress("john.doe@test.com");
        user.setUserName("johndoe");
        user.setUserType(Set.of(Role.ADMIN_ROLE));

        // when
        UserDto result = UserMapper.mapToUserDto(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(user.getId());
        assertThat(result.firstName()).isEqualTo(user.getFirstName());
        assertThat(result.lastName()).isEqualTo(user.getLastName());
        assertThat(result.emailAddress()).isEqualTo(user.getEmailAddress());
        assertThat(result.userName()).isEqualTo(user.getUserName());
        assertThat(result.userType()).isEqualTo(user.getUserType());
    }

    @Test
    @DisplayName("should return null when input User is null")
    void shouldReturnNullWhenUserIsNull() {
        // when
        UserDto result = UserMapper.mapToUserDto(null);

        // then
        assertThat(result).isNull();
    }
}
