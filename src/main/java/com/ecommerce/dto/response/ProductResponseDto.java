package com.ecommerce.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private CategoryResponseDto category;
    private Integer stock;
    private Boolean active;
} 