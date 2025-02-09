package com.ecommerce.service;

import com.ecommerce.dto.request.ProductRequestDto;
import com.ecommerce.dto.response.ProductResponseDto;
import java.util.List;

public interface ProductService {
    List<ProductResponseDto> getAllProducts();
    ProductResponseDto getProductById(Long id);
    ProductResponseDto createProduct(ProductRequestDto requestDto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);
    void deleteProduct(Long id);
    List<ProductResponseDto> getFeaturedProducts();
    List<ProductResponseDto> getNewArrivals();
    List<ProductResponseDto> getPopularProducts();
    List<ProductResponseDto> getDiscountedProducts();
    List<ProductResponseDto> getProductsByCategory(Long categoryId);
    List<ProductResponseDto> getProductsByVendor(Long vendorId);
    List<ProductResponseDto> searchProducts(String query);
} 