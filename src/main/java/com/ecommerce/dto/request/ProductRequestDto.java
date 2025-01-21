package com.ecommerce.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Integer stock;
} 