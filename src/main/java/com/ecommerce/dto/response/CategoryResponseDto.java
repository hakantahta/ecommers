package com.ecommerce.dto.response;

import com.ecommerce.model.CategoryType;
import lombok.Data;
import java.util.List;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String icon;
    private CategoryType type;
    private Long parentId;
    private List<CategoryResponseDto> subCategories;
} 