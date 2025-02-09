package com.ecommerce.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private String imageUrl;
    private Integer stock;
    private Boolean isActive;
    private Boolean isFeatured;
    private Integer orderCount;
    private Double rating;
    private Integer reviewCount;
    private Long categoryId;
    private String categoryName;
    private Long vendorId;
    private String vendorName;
} 