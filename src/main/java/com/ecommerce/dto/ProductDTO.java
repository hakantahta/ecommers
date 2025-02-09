package com.ecommerce.dto;

import com.ecommerce.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
    private ProductStatus status;
    private Long vendorId;
    private Boolean isActive;
    private String categoryName;
    private String vendorName;
    private Double vendorRating;
    private Double rating;
    private Integer reviewCount;
    private Boolean isInStock;
    private LocalDateTime createdAt;
} 