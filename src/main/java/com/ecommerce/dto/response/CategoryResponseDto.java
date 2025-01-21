package com.ecommerce.dto.response;

import com.ecommerce.model.CategoryType;
import lombok.Data;
import java.util.List;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private CategoryType type;
    private Long parentCategoryId;
    private List<CategoryResponseDto> subCategories;
} 