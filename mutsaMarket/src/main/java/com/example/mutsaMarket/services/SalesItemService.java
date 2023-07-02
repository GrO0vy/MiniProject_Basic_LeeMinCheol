package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dao.SalesItemDao;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;

    public SalesItemDao registerItem(SalesItemDao salesItemDao){
        SalesItemEntity salesItemEntity = new SalesItemEntity();

        salesItemEntity.setTitle(salesItemDao.getTitle());
        salesItemEntity.setDescription(salesItemDao.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDao.getMinPriceWanted());
        salesItemEntity.setWriter(salesItemDao.getWriter());
        salesItemEntity.setPassword(salesItemDao.getPassword());

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDao.fromEntity(salesItemEntity);
    }

    public SalesItemDao readItemById(Integer itemId){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(optionalEntity.isPresent()){
            SalesItemEntity entity = optionalEntity.get();
            return SalesItemDao.fromEntity(entity);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
