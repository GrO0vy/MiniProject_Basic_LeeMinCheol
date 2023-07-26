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

    public String updateNegotiation(Integer itemId, Integer proposalId, NegotiationDto negotiationDto){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(!negotiationRepository.existsById(proposalId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String proposalName = negotiationDto.getWriter();
        String proposalPassword = negotiationDto.getPassword();
        Integer suggestedPrice = negotiationDto.getSuggestedPrice();
        String status = negotiationDto.getStatus();

        // 물품 등록자일경우 제안 상태를 수락 또는 거절로 변경 가능
        if(isOwner(itemId, proposalName, proposalPassword)){
            NegotiationEntity entity = negotiationRepository.findById(proposalId).get();
            if(status != null){
                entity.setStatus(status);
                negotiationRepository.save(entity);
                return "제안의 상태가 변경되었습니다.";
            }
            else throw new IllegalArgumentException();
        }
        // 그렇지 않고 제안을 등록한 사용자이면
        else if(isValidUser(proposalId, proposalName, proposalPassword)){
            NegotiationEntity negotiationEntity = negotiationRepository.findById(proposalId).get();
            if(suggestedPrice != null){
                negotiationEntity.setSuggestedPrice(suggestedPrice);
                negotiationRepository.save(negotiationEntity);
                return "제안이 수정되었습니다.";
            }
            else if(negotiationEntity.getStatus().equals("수락") && status.equals("확정")){
                negotiationEntity.setStatus("확정");
                SalesItemEntity salesItemEntity = salesItemRepository.findById(itemId).get();
                salesItemEntity.setStatus("판매 완료");
                negotiationRepository.save(negotiationEntity);
                salesItemRepository.save(salesItemEntity);
                List<NegotiationEntity> entities = negotiationRepository.findAllByItemIdAndIdNot(itemId, proposalId);
                for(NegotiationEntity entity: entities){
                    entity.setStatus("거절");
                    negotiationRepository.save(entity);
                }
                return "구매가 확정되었습니다.";
            }
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 유효한 사용자가 아니면
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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

    public boolean isValidUser(Integer proposalId, String writer, String password){
        NegotiationEntity entity = negotiationRepository.findById(proposalId).get();

        return entity.getWriter().equals(writer) && entity.getPassword().equals(password);
    }
}
