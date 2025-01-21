package com.ecommerce.dto.response;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String type; // MAIN veya SUB
    private Long parentCategoryId; // Eğer alt kategori ise
} 