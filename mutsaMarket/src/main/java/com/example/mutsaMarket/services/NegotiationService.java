package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.entity.NegotiationEntity;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.repositories.NegotiationRepository;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<NegotiationDto> readNegotiationAll(Integer itemId, Integer page, Integer size, String writer, String password){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 페이지 정보 설정
        Pageable pageable = PageRequest.of(page, size);
        Page<NegotiationEntity> negotiationEntityPage;
        Page<NegotiationDto> negotiationDtoPage;

        // 물품 등록자 정보 가져오기
        SalesItemEntity ownerData = salesItemRepository.findById(itemId).get();
        String ownerId = ownerData.getWriter();
        String ownerPassword = ownerData.getPassword();

        // 물품 등록자와 입력 받은 아이디가 같고
        if(writer.equals(ownerId)){
            // 물품 등록자의 패스워드가 같으면
            if(password.equals(ownerPassword)){
                // 해당 물품의 모든 제안을 가져와서 리턴
                negotiationEntityPage = negotiationRepository.findAllByItemId(itemId, pageable);
                negotiationDtoPage = negotiationEntityPage.map(NegotiationDto::fromEntity);
                return negotiationDtoPage;
            }
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 물품 등록자가 아니면
        else{
            // 입력 받은 아이디로 등록된 제안 가져옴
            negotiationEntityPage = negotiationRepository.findAllByWriter(writer, pageable);
            // 비어있으면 BAD Request
            if(negotiationEntityPage.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            negotiationDtoPage = negotiationEntityPage.map(NegotiationDto::fromEntity);

            return negotiationDtoPage;
        }

    }
}
