package com.ecommerce.dto.response;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long userId;
    private Long productId; // Ürün ID'si
    private Integer quantity;
} 