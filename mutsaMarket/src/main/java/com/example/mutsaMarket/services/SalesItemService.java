package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dao.SalesItemDao;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
