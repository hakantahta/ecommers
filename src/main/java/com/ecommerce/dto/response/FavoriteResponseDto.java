package com.ecommerce.dto.response;

import com.ecommerce.model.Product;
import lombok.Data;

@Data
public class FavoriteResponseDto {
    private Long id;
    private Long userId;
    private ProductResponseDto product;
} 