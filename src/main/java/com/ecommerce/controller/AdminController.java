package com.ecommerce.controller;

import com.ecommerce.dto.response.UserResponseDto;
import com.ecommerce.model.AccountStatus;
import com.ecommerce.model.UserRole;
import com.ecommerce.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    // User Management
    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @GetMapping("/users/role/{role}")
    @Operation(summary = "Get users by role")
    public ResponseEntity<Page<UserResponseDto>> getUsersByRole(
            @PathVariable UserRole role,
            Pageable pageable) {
        return ResponseEntity.ok(adminService.getUsersByRole(role, pageable));
    }

    @PatchMapping("/users/{userId}/status")
    @Operation(summary = "Update user status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody AccountStatus status) {
        adminService.updateUserStatus(userId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/ban")
    @Operation(summary = "Ban user")
    public ResponseEntity<Void> banUser(
            @PathVariable Long userId,
            @RequestBody String reason) {
        adminService.banUser(userId, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/unban")
    @Operation(summary = "Unban user")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        adminService.unbanUser(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    // Role Management
    @PatchMapping("/users/{userId}/role")
    @Operation(summary = "Update user role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long userId,
            @RequestBody UserRole newRole) {
        adminService.updateUserRole(userId, newRole);
        return ResponseEntity.ok().build();
    }

    // System Statistics
    @GetMapping("/statistics")
    @Operation(summary = "Get system statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(Map.of(
                "totalUsers", adminService.getTotalUsers(),
                "totalOrders", adminService.getTotalOrders(),
                "totalRevenue", adminService.getTotalRevenue()
        ));
    }

    // Vendor Management
    @GetMapping("/vendors/pending")
    @Operation(summary = "Get pending vendor applications")
    public ResponseEntity<Page<UserResponseDto>> getPendingVendors(Pageable pageable) {
        return ResponseEntity.ok(adminService.getPendingVendors(pageable));
    }

    @PostMapping("/vendors/{userId}/approve")
    @Operation(summary = "Approve vendor application")
    public ResponseEntity<Void> approveVendor(@PathVariable Long userId) {
        adminService.approveVendor(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/vendors/{userId}/reject")
    @Operation(summary = "Reject vendor application")
    public ResponseEntity<Void> rejectVendor(
            @PathVariable Long userId,
            @RequestBody String reason) {
        adminService.rejectVendor(userId, reason);
        return ResponseEntity.ok().build();
    }

    // Product Management
    @PostMapping("/products/{productId}/approve")
    @Operation(summary = "Approve product")
    public ResponseEntity<Void> approveProduct(@PathVariable Long productId) {
        adminService.approveProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/{productId}/reject")
    @Operation(summary = "Reject product")
    public ResponseEntity<Void> rejectProduct(
            @PathVariable Long productId,
            @RequestBody String reason) {
        adminService.rejectProduct(productId, reason);
        return ResponseEntity.ok().build();
    }

    // Order Management
    @PostMapping("/orders/{orderId}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody String reason) {
        adminService.cancelOrder(orderId, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders/{orderId}/refund")
    @Operation(summary = "Refund order")
    public ResponseEntity<Void> refundOrder(@PathVariable Long orderId) {
        adminService.refundOrder(orderId);
        return ResponseEntity.ok().build();
    }
} 
