package com.example.amdaeus.repository;

import com.example.amdaeus.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
/*
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("should find user by email address")
    void shouldFindUserByEmailAddress() {
        // given
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmailAddress("john.doe@test.com");
        user.setUserName("johndoe");
        user.setPassword("password");

        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByEmailAddress("john.doe@test.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserName()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("should return empty when user with given email does not exist")
    void shouldReturnEmptyWhenEmailNotFound() {
        // when
        Optional<User> result = userRepository.findByEmailAddress("not.exists@test.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should find user by username")
    void shouldFindUserByUserName() {
        // given
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmailAddress("jane.smith@test.com");
        user.setUserName("janesmith");
        user.setPassword("password");

        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByUserName("janesmith");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmailAddress()).isEqualTo("jane.smith@test.com");
    }
    */

}
