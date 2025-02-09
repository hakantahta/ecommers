package com.ecommerce.service.impl;

import com.ecommerce.dto.request.ProductRequestDto;
import com.ecommerce.dto.response.ProductResponseDto;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.ProductStatus;
import com.ecommerce.model.Vendor;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.VendorRepository;
import com.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToResponseDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setDiscountedPrice(requestDto.getDiscountedPrice());
        product.setImageUrl(requestDto.getImageUrl());
        product.setStock(requestDto.getStock());
        product.setIsActive(true);
        product.setIsFeatured(false);
        product.setOrderCount(0);
        product.setRating(0.0);
        product.setReviewCount(0);
        
        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        if (requestDto.getVendorId() != null) {
            Vendor vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));
            product.setVendor(vendor);
        }
        
        product.setStatus(ProductStatus.ACTIVE);
        
        Product savedProduct = productRepository.save(product);
        return convertToResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setDiscountedPrice(requestDto.getDiscountedPrice());
        product.setImageUrl(requestDto.getImageUrl());
        product.setStock(requestDto.getStock());
        
        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        
        Product updatedProduct = productRepository.save(product);
        return convertToResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDto> getFeaturedProducts() {
        try {
            return productRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedAtDesc()
                    .stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching featured products: " + e.getMessage());
        }
    }

    @Override
    public List<ProductResponseDto> getNewArrivals() {
        return productRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getPopularProducts() {
        return productRepository.findTop10ByOrderByOrderCountDesc()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getDiscountedProducts() {
        return productRepository.findByDiscountedPriceIsNotNullAndIsActiveTrueOrderByDiscountedPriceAsc()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndIsActiveTrue(categoryId)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByVendor(Long vendorId) {
        return productRepository.findByVendorIdAndIsActiveTrue(vendorId)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(query)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private ProductResponseDto convertToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setStock(product.getStock());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setOrderCount(product.getOrderCount());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        if (product.getVendor() != null) {
            dto.setVendorId(product.getVendor().getId());
            dto.setVendorName(product.getVendor().getStoreName());
        }
        
        return dto;
    }
} 
