package com.ecommerce.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemRequestDto {
    private Long userId;
    private Long productId; // Ürün ID'si
    private Integer quantity;
    private BigDecimal price;
} 