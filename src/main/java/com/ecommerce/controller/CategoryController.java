package com.ecommerce.controller;

import com.ecommerce.dto.request.CategoryRequestDto;
import com.ecommerce.dto.response.CategoryResponseDto;
import com.ecommerce.model.Category;
import com.ecommerce.model.CategoryType;
import com.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        // DTO dönüşümü yapılabilir
        return ResponseEntity.ok(categories.stream().map(this::convertToResponseDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(convertToResponseDto(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setType(CategoryType.valueOf(categoryRequestDto.getType().toUpperCase()));
        category.setParentCategoryId(categoryRequestDto.getParentCategoryId());
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(convertToResponseDto(createdCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setId(id);
        category.setName(categoryRequestDto.getName());
        category.setType(CategoryType.valueOf(categoryRequestDto.getType().toUpperCase()));
        category.setParentCategoryId(categoryRequestDto.getParentCategoryId());
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(convertToResponseDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryResponseDto convertToResponseDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        dto.setParentCategoryId(category.getParentCategoryId());
        return dto;
    }
} 