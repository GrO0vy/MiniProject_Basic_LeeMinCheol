package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.entity.NegotiationEntity;
import com.example.mutsaMarket.repositories.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;

    public NegotiationDto registerNegotiation(Integer itemId, NegotiationDto negotiationDto){
        NegotiationEntity entity = new NegotiationEntity();
        entity.setItemId(itemId);
        entity.setSuggestedPrice(negotiationDto.getSuggestedPrice());
        entity.setStatus("제안");
        entity.setWriter(negotiationDto.getWriter());
        entity.setPassword(negotiationDto.getPassword());

        entity = negotiationRepository.save(entity);

        return NegotiationDto.fromEntity(entity);
    }
}
