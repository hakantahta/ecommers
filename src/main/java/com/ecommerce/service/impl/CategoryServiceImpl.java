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
        category.setType(requestDto.getType());
        category.setParentCategoryId(requestDto.getParentCategoryId());
        
        Category savedCategory = categoryRepository.save(category);
        return convertToResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setName(requestDto.getName());
        category.setType(requestDto.getType());
        category.setParentCategoryId(requestDto.getParentCategoryId());
        
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
        dto.setType(category.getType());
        dto.setParentCategoryId(category.getParentCategoryId());
        
        if (category.getSubCategories() != null) {
            dto.setSubCategories(
                category.getSubCategories().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
} 