package com.example.mutsaMarket.dto;

import lombok.Data;

@Data
public class NegotiationDto {
    private Integer id;
    private Integer itemId;
    private Integer suggestedPrice;
    private String status;
    private String writer;
    private String password;
}
