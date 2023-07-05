package com.example.mutsaMarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NegotiationDto {
    private Integer id;
    private Integer itemId;
    private Integer suggestedPrice;
    private String status;
    private String writer;
    private String password;
}
