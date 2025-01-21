package com.ecommerce.dto.request;

import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private String type; // MAIN veya SUB
    private Long parentCategoryId; // Eğer alt kategori ise
} 