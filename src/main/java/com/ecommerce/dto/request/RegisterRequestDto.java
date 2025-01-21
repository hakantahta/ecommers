package com.ecommerce.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String name;
} 