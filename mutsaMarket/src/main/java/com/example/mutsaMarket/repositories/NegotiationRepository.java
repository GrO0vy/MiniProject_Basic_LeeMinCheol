package com.example.mutsaMarket.repositories;

import com.example.mutsaMarket.entity.NegotiationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity, Integer> {
    Page<NegotiationEntity> findAllByItemId(Integer itemId, Pageable pageable);
    List<NegotiationEntity> findAllByItemId(Integer itemId);
    Page<NegotiationEntity> findAllByItemIdAndWriterAndPassword(Integer itemId, String writer, String password, Pageable pageable);
    List<NegotiationEntity> findAllByItemIdAndIdNot(Integer itemId, Integer proposalId);
    Optional<NegotiationEntity> findAllByIdAndWriterAndPassword(Integer proposalId, String writer, String password);
}
