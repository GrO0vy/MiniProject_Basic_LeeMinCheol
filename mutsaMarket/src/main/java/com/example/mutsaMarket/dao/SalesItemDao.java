package com.example.mutsaMarket.dao;

import com.example.mutsaMarket.entity.SalesItemEntity;
import lombok.Data;

@Data
public class SalesItemDao {
    private Integer id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;
    private String writer;
    private String password;

    public static SalesItemDao fromEntity(SalesItemEntity entity){
        SalesItemDao salesItemDao = new SalesItemDao();
        salesItemDao.setId(entity.getId());
        salesItemDao.setTitle(entity.getTitle());
        salesItemDao.setDescription(entity.getDescription());
        salesItemDao.setImageUrl(entity.getImageUrl());
        salesItemDao.setMinPriceWanted(entity.getMinPriceWanted());
        salesItemDao.setStatus(entity.getStatus());
        salesItemDao.setWriter(entity.getWriter());
        salesItemDao.setPassword(entity.getPassword());

        return salesItemDao;
    }
}
