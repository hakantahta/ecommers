package com.ecommerce.service.impl;

import com.ecommerce.dto.request.CategoryRequestDto;
import com.ecommerce.dto.response.CategoryResponseDto;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToResponseDto(category);
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = new Category();
        category.setName(requestDto.getName());
        category.setIcon(requestDto.getIcon());
        category.setType(requestDto.getType());
        
        // Parent category varsa, set et
        if (requestDto.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parentCategory);
        }
        
        Category savedCategory = categoryRepository.save(category);
        return convertToResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setName(requestDto.getName());
        category.setIcon(requestDto.getIcon());
        category.setType(requestDto.getType());
        
        // Parent category güncelleme
        if (requestDto.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }
        
        Category updatedCategory = categoryRepository.save(category);
        return convertToResponseDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto convertToResponseDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());
        dto.setType(category.getType());
        
        // Parent category bilgisini set et
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        
        // Alt kategorileri recursive olarak dönüştür
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            dto.setSubCategories(
                category.getSubCategories().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
} 