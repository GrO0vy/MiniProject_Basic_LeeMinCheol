package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.entity.NegotiationEntity;
import com.example.mutsaMarket.repositories.NegotiationRepository;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository salesItemRepository;

    public NegotiationDto registerNegotiation(Integer itemId, NegotiationDto negotiationDto){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

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
