package com.example.mutsaMarket.repositories;

import com.example.mutsaMarket.entity.CommentEntity;
import com.example.mutsaMarket.entity.SalesItemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllBySalesItem(SalesItemEntity salesItem, Pageable pageable);
    @Transactional
    void deleteAllBySalesItem(SalesItemEntity salesItem);
}
