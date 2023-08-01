package com.example.mutsaMarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String userPassword;
    @NotBlank
    private String passwordCheck;
    private String phone;
    private String email;
    private String address;
}
