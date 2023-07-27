package com.example.mutsaMarket.repositories;

import com.example.mutsaMarket.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByUserId(String userId);
    Optional<UserEntity> findByUserId(String userId);
}
