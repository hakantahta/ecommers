package com.ecommerce.dto.request;

import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long userId;
    private Long productId; // Ürün ID'si
    private Integer quantity;
} 