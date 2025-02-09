package com.ecommerce.dto;

import com.ecommerce.model.Banner.BannerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String link;
    private BannerType type;
    private Integer displayOrder;
    private Boolean isActive;
} 