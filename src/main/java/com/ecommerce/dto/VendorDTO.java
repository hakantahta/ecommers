package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDTO {
    private Long id;
    private Long userId;
    private String storeName;
    private String storeDescription;
    private String contactEmail;
    private String contactPhone;
    private Double rating;
    private Integer reviewCount;
    private Integer totalOrders;
    private Boolean isActive;
} 