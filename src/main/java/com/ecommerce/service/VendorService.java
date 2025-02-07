package com.ecommerce.service;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.request.PromotionRequest;
import com.ecommerce.dto.request.VendorProfileUpdateRequest;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.Promotion;
import com.ecommerce.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VendorService {
    // Profile Management
    void updateProfile(Long vendorId, VendorProfileUpdateRequest request);
    
    // Product Management
    Product addProduct(Long vendorId, ProductRequest request);
    Product updateProduct(Long vendorId, Long productId, ProductRequest request);
    void deleteProduct(Long vendorId, Long productId);
    void updateProductStock(Long vendorId, Long productId, Integer quantity);
    void updateProductStatus(Long vendorId, Long productId, String status);
    Page<Product> getVendorProducts(Long vendorId, Pageable pageable);
    
    // Order Management
    Page<Order> getVendorOrders(Long vendorId, Pageable pageable);
    Order getOrderDetails(Long vendorId, Long orderId);
    void updateOrderStatus(Long vendorId, Long orderId, String status);
    void processRefundRequest(Long vendorId, Long orderId, boolean approved, String reason);
    
    // Promotion Management
    Promotion createPromotion(Long vendorId, PromotionRequest request);
    Promotion updatePromotion(Long vendorId, Long promotionId, PromotionRequest request);
    void deletePromotion(Long vendorId, Long promotionId);
    Page<Promotion> getVendorPromotions(Long vendorId, Pageable pageable);
    
    // Customer Service
    List<Review> getProductReviews(Long vendorId, Long productId);
    void respondToReview(Long vendorId, Long reviewId, String response);
    
    // Analytics and Reports
    Map<String, BigDecimal> getRevenueStats(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Long> getOrderStats(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> getProductStats(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Financial Management
    Map<String, BigDecimal> getFinancialSummary(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);
    List<Map<String, Object>> getTransactionHistory(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);
} 