package com.ecommerce.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private String imageUrl;
    private Integer stock;
    private Long categoryId;
    private Long vendorId;
} 