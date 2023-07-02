package com.example.mutsaMarket.dao;

import com.example.mutsaMarket.entity.SalesItemEntity;
import lombok.Data;

@Data
public class SalesItemDao {
    private String title;
    private String description;
    private Integer minPriceWanted;
    private String writer;
    private String password;

    public static SalesItemDao fromEntity(SalesItemEntity entity){
        SalesItemDao salesItemDao = new SalesItemDao();
        salesItemDao.setTitle(entity.getTitle());
        salesItemDao.setDescription(entity.getDescription());
        salesItemDao.setMinPriceWanted(entity.getMinPriceWanted());
        salesItemDao.setWriter(entity.getWriter());
        salesItemDao.setPassword(entity.getPassword());

        return salesItemDao;
    }
}
