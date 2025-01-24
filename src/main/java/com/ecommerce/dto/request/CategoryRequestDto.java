package com.ecommerce.dto.request;

import com.ecommerce.model.CategoryType;
import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private CategoryType type;
    private Long parentId;
} 