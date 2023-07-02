package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;

    public SalesItemDto registerItem(SalesItemDto salesItemDao){
        SalesItemEntity salesItemEntity = new SalesItemEntity();

        salesItemEntity.setTitle(salesItemDao.getTitle());
        salesItemEntity.setDescription(salesItemDao.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDao.getMinPriceWanted());
        salesItemEntity.setWriter(salesItemDao.getWriter());
        salesItemEntity.setPassword(salesItemDao.getPassword());

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public Page<SalesItemDto> readItemAll(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<SalesItemEntity> salesItemEntityPage = salesItemRepository.findAll(pageable);
        Page<SalesItemDto> salesItemDtoPage = salesItemEntityPage.map(SalesItemDto::fromEntity);

        return salesItemDtoPage;

    }
    public SalesItemDto readItemById(Integer itemId){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(optionalEntity.isPresent()){
            SalesItemEntity entity = optionalEntity.get();
            return SalesItemDto.fromEntity(entity);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public SalesItemDto updateItem(Integer itemId, SalesItemDto salesItemDto){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(optionalEntity.isPresent()){
            SalesItemEntity salesItemEntity = optionalEntity.get();

            salesItemEntity.setTitle(salesItemDto.getTitle());
            salesItemEntity.setDescription(salesItemDto.getDescription());
            salesItemEntity.setMinPriceWanted(salesItemDto.getMinPriceWanted());
            salesItemEntity.setWriter(salesItemDto.getWriter());
            salesItemEntity.setPassword(salesItemDto.getPassword());

            salesItemEntity = salesItemRepository.save(salesItemEntity);

            return SalesItemDto.fromEntity(salesItemEntity);
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
