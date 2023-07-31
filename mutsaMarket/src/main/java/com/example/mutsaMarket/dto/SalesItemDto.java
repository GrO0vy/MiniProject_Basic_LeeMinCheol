package com.example.mutsaMarket.dto;

import com.example.mutsaMarket.entity.SalesItemEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesItemDto {
    private Integer id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;

    public static SalesItemDto fromEntity(SalesItemEntity entity){
        SalesItemDto salesItemDao = new SalesItemDto();
        salesItemDao.setId(entity.getId());
        salesItemDao.setTitle(entity.getTitle());
        salesItemDao.setDescription(entity.getDescription());
        salesItemDao.setImageUrl(entity.getImageUrl());
        salesItemDao.setMinPriceWanted(entity.getMinPriceWanted());
        salesItemDao.setStatus(entity.getStatus());

        return salesItemDao;
    }
}
