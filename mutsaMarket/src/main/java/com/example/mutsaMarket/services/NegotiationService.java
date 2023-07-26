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

import java.util.List;
import java.util.Optional;

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


        // 물품 등록자이면
        if(isOwner(itemId, writer, password)){
            negotiationEntityPage = negotiationRepository.findAllByItemId(itemId, pageable);
            negotiationDtoPage = negotiationEntityPage.map(NegotiationDto::fromEntity);
            return negotiationDtoPage;
        }
        // 물품 등록자가 아니면
        else {
            // 입력 받은 아이디와 패스워드가 일치하는 제안 가져옴
            negotiationEntityPage = negotiationRepository.findAllByItemIdAndWriterAndPassword(itemId, writer, password, pageable);
            // 비어있으면 BAD Request
            if(negotiationEntityPage.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            negotiationDtoPage = negotiationEntityPage.map(NegotiationDto::fromEntity);

            return negotiationDtoPage;
        }
    }

    public int updateNegotiation(Integer itemId, Integer proposalId, NegotiationDto negotiationDto){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<NegotiationEntity> negotiationEntityList = negotiationRepository.findAllByItemId(itemId);

        SalesItemEntity ownerData = salesItemRepository.findById(itemId).get();
        String ownerId = ownerData.getWriter();
        String ownerPassword = ownerData.getPassword();

        NegotiationEntity negotiationEntity = null;

        for(NegotiationEntity entity : negotiationEntityList){
            if(proposalId == entity.getId()){
                negotiationEntity = entity;
                break;
            }
        }

        if(negotiationEntity.equals(null)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else{
            // 물품 가격제안을 한 사용자 일 때
             if(negotiationEntity.getWriter().equals(negotiationDto.getWriter())){
                 if(negotiationEntity.getPassword().equals(negotiationDto.getPassword())){
                     // 상태가 수락일 때 사용자가 구매확정가능
                     if(negotiationEntity.getStatus().equals("수락")){
                         negotiationEntity.setStatus(negotiationEntity.getStatus());
                         negotiationRepository.save(negotiationEntity);
                         ownerData.setStatus("판매 완료");
                         salesItemRepository.save(ownerData);
                         return 1;
                     }
                     // 수락이 아닐 때는 가격 제시 가능
                     else if(!negotiationEntity.getSuggestedPrice().equals(null)){
                         negotiationEntity.setSuggestedPrice(negotiationDto.getSuggestedPrice());
                         negotiationRepository.save(negotiationEntity);
                         return 2;
                     }
                     else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                 }
                 else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
             }
             // 물품 등록자일경우 제안 상태를 수락 또는 거절로 변경 가능
             else if(negotiationEntity.getWriter().equals(ownerId)){
                 if(negotiationEntity.getPassword().equals(ownerPassword)){
                     negotiationEntity.setStatus(negotiationDto.getStatus());
                     negotiationRepository.save(negotiationEntity);
                     return 3;
                 }
                 else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
             }
             else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteNegotiation(Integer itemId, Integer proposalId, NegotiationDto negotiationDto){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        List<NegotiationEntity> negotiationEntityList = negotiationRepository.findAllByItemId(itemId);

        NegotiationEntity negotiationEntity = null;

        for(NegotiationEntity entity : negotiationEntityList){
            if(proposalId == entity.getId()){
                negotiationEntity = entity;
                break;
            }
        }

        if(negotiationEntity.equals(null)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else{
            if(negotiationEntity.getWriter().equals(negotiationDto.getWriter())){
                if(negotiationEntity.getPassword().equals(negotiationDto.getPassword())){
                    negotiationRepository.delete(negotiationEntity);
                }
                else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public boolean isOwner(Integer itemId, String writer, String password){
        SalesItemEntity entity = salesItemRepository.findById(itemId).get();
        String ownerName = entity.getWriter();
        String ownerPassword = entity.getPassword();

        return ownerName.equals(writer) && ownerPassword.equals(password);
    }
}
