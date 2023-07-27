package com.example.mutsaMarket.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String userId;
    private String userPassword;
    private String passwordCheck;
    private String phone;
    private String email;
    private String address;
}
