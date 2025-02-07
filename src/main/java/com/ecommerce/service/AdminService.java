package com.ecommerce.service;

import com.ecommerce.dto.response.UserResponseDto;
import com.ecommerce.model.AccountStatus;
import com.ecommerce.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    // User Management
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    Page<UserResponseDto> getUsersByRole(UserRole role, Pageable pageable);
    void updateUserStatus(Long userId, AccountStatus status);
    void banUser(Long userId, String reason);
    void unbanUser(Long userId);
    void deleteUser(Long userId);

    // Role Management
    void updateUserRole(Long userId, UserRole newRole);
    
    // System Statistics
    long getTotalUsers();
    long getTotalOrders();
    double getTotalRevenue();
    
    // Vendor Management
    void approveVendor(Long userId);
    void rejectVendor(Long userId, String reason);
    Page<UserResponseDto> getPendingVendors(Pageable pageable);
    
    // Product Management
    void approveProduct(Long productId);
    void rejectProduct(Long productId, String reason);
    
    // Order Management
    void cancelOrder(Long orderId, String reason);
    void refundOrder(Long orderId);
} 