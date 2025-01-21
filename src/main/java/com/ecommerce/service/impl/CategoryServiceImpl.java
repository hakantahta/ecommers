package com.ecommerce.service.impl;

import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
} 