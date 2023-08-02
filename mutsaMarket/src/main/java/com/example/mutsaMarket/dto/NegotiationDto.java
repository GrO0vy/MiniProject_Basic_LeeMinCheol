package com.example.mutsaMarket.dto;

import com.example.mutsaMarket.entity.NegotiationEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NegotiationDto {
    private Integer id;
    private Integer itemId;
    private Integer suggestedPrice;
    private String status;
    private String userId;

    public static NegotiationDto fromEntity(NegotiationEntity entity){
        NegotiationDto negotiationDto = new NegotiationDto();

        negotiationDto.setId(entity.getId());
        negotiationDto.setSuggestedPrice(entity.getSuggestedPrice());
        negotiationDto.setStatus(entity.getStatus());
        negotiationDto.setUserId(entity.getUser().getUserId());

        return negotiationDto;
    }
}
