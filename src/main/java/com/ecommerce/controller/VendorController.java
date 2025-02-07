package com.ecommerce.controller;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.request.PromotionRequest;
import com.ecommerce.dto.request.VendorProfileUpdateRequest;
import com.ecommerce.model.*;
import com.ecommerce.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VENDOR')")
@Tag(name = "Vendor", description = "Vendor operations API")
@SecurityRequirement(name = "bearerAuth")
public class VendorController {

    private final VendorService vendorService;

    // Profile Management
    @PatchMapping("/profile")
    @Operation(summary = "Update vendor profile")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VendorProfileUpdateRequest request) {
        vendorService.updateProfile(getUserId(userDetails), request);
        return ResponseEntity.ok().build();
    }

    // Product Management
    @PostMapping("/products")
    @Operation(summary = "Add new product")
    public ResponseEntity<Product> addProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(vendorService.addProduct(getUserId(userDetails), request));
    }

    @PutMapping("/products/{productId}")
    @Operation(summary = "Update product")
    public ResponseEntity<Product> updateProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(vendorService.updateProduct(getUserId(userDetails), productId, request));
    }

    @DeleteMapping("/products/{productId}")
    @Operation(summary = "Delete product")
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        vendorService.deleteProduct(getUserId(userDetails), productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/products/{productId}/stock")
    @Operation(summary = "Update product stock")
    public ResponseEntity<Void> updateProductStock(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        vendorService.updateProductStock(getUserId(userDetails), productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/products/{productId}/status")
    @Operation(summary = "Update product status")
    public ResponseEntity<Void> updateProductStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @RequestParam String status) {
        vendorService.updateProductStatus(getUserId(userDetails), productId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    @Operation(summary = "Get vendor products")
    public ResponseEntity<Page<Product>> getProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(vendorService.getVendorProducts(getUserId(userDetails), pageable));
    }

    // Order Management
    @GetMapping("/orders")
    @Operation(summary = "Get vendor orders")
    public ResponseEntity<Page<Order>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(vendorService.getVendorOrders(getUserId(userDetails), pageable));
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Get order details")
    public ResponseEntity<Order> getOrderDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(vendorService.getOrderDetails(getUserId(userDetails), orderId));
    }

    @PatchMapping("/orders/{orderId}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<Void> updateOrderStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId,
            @RequestParam String status) {
        vendorService.updateOrderStatus(getUserId(userDetails), orderId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders/{orderId}/refund")
    @Operation(summary = "Process refund request")
    public ResponseEntity<Void> processRefundRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String reason) {
        vendorService.processRefundRequest(getUserId(userDetails), orderId, approved, reason);
        return ResponseEntity.ok().build();
    }

    // Promotion Management
    @PostMapping("/promotions")
    @Operation(summary = "Create promotion")
    public ResponseEntity<Promotion> createPromotion(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PromotionRequest request) {
        return ResponseEntity.ok(vendorService.createPromotion(getUserId(userDetails), request));
    }

    @PutMapping("/promotions/{promotionId}")
    @Operation(summary = "Update promotion")
    public ResponseEntity<Promotion> updatePromotion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long promotionId,
            @Valid @RequestBody PromotionRequest request) {
        return ResponseEntity.ok(vendorService.updatePromotion(getUserId(userDetails), promotionId, request));
    }

    @DeleteMapping("/promotions/{promotionId}")
    @Operation(summary = "Delete promotion")
    public ResponseEntity<Void> deletePromotion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long promotionId) {
        vendorService.deletePromotion(getUserId(userDetails), promotionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/promotions")
    @Operation(summary = "Get vendor promotions")
    public ResponseEntity<Page<Promotion>> getPromotions(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(vendorService.getVendorPromotions(getUserId(userDetails), pageable));
    }

    // Review Management
    @GetMapping("/products/{productId}/reviews")
    @Operation(summary = "Get product reviews")
    public ResponseEntity<List<Review>> getProductReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        return ResponseEntity.ok(vendorService.getProductReviews(getUserId(userDetails), productId));
    }

    @PostMapping("/reviews/{reviewId}/respond")
    @Operation(summary = "Respond to review")
    public ResponseEntity<Void> respondToReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId,
            @RequestBody String response) {
        vendorService.respondToReview(getUserId(userDetails), reviewId, response);
        return ResponseEntity.ok().build();
    }

    // Analytics and Reports
    @GetMapping("/analytics/revenue")
    @Operation(summary = "Get revenue statistics")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueStats(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(vendorService.getRevenueStats(getUserId(userDetails), startDate, endDate));
    }

    @GetMapping("/analytics/orders")
    @Operation(summary = "Get order statistics")
    public ResponseEntity<Map<String, Long>> getOrderStats(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(vendorService.getOrderStats(getUserId(userDetails), startDate, endDate));
    }

    @GetMapping("/analytics/products")
    @Operation(summary = "Get product statistics")
    public ResponseEntity<Map<String, Object>> getProductStats(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(vendorService.getProductStats(getUserId(userDetails), startDate, endDate));
    }

    // Financial Management
    @GetMapping("/finance/summary")
    @Operation(summary = "Get financial summary")
    public ResponseEntity<Map<String, BigDecimal>> getFinancialSummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(vendorService.getFinancialSummary(getUserId(userDetails), startDate, endDate));
    }

    @GetMapping("/finance/transactions")
    @Operation(summary = "Get transaction history")
    public ResponseEntity<List<Map<String, Object>>> getTransactionHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(vendorService.getTransactionHistory(getUserId(userDetails), startDate, endDate));
    }

    private Long getUserId(UserDetails userDetails) {
        return ((User) userDetails).getId();
    }
} 