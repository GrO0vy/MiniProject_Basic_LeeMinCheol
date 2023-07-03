package com.example.mutsaMarket.repositories;

import com.example.mutsaMarket.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
}
