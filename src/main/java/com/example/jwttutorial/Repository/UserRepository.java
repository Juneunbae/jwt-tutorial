package com.example.jwttutorial.Repository;

import com.example.jwttutorial.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Boolean existsByUsername(String username);
}
