package com.example.mutsaMarket.repositories;

import com.example.mutsaMarket.entity.NegotiationEntity;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity, Integer> {
    Page<NegotiationEntity> findAllBySalesItem(SalesItemEntity salesItem, Pageable pageable);
    Page<NegotiationEntity> findAllBySalesItemAndUser(SalesItemEntity salesItem, UserEntity user, Pageable pageable);
    List<NegotiationEntity> findAllBySalesItemAndIdNot(SalesItemEntity salesItem, Integer proposalId);
    Optional<NegotiationEntity> findAllByIdAndUser(Integer proposalId, UserEntity user);

    @Transactional
    void deleteAllBySalesItem(SalesItemEntity salesItem);
}
