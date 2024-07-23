package com.example.jwttutorial.Repository;

import com.example.jwttutorial.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Boolean existsByUsername(String username);

    // UserName 을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    UserEntity findByUsername(String username);
}