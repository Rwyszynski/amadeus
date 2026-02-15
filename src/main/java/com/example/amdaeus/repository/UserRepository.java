package com.example.amdaeus.repository;

import com.example.amdaeus.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAddress(String email);

    Optional<User> findByUserName(String userName);

}
