package com.example.mutsaMarket.repositories;

import com.example.mutsaMarket.entity.CommentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
//    List<CommentEntity> findAllByItemId(Integer itemId);

    Page<CommentEntity> findAllByItemId(Integer itemId, Pageable pageable);
    @Transactional
    void deleteAllByItemId(Integer itemId);
}
