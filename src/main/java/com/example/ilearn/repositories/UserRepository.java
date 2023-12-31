package com.example.ilearn.repositories;

import com.example.ilearn.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);
    UserEntity findByEmail(String email);
}

