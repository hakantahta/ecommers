package com.ecommerce.service;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.request.CustomerProfileUpdateRequest;
import com.ecommerce.dto.request.ReviewRequest;
import com.ecommerce.model.Address;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    // Profile Management
    void updateProfile(Long userId, CustomerProfileUpdateRequest request);
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    // Address Management
    List<Address> getAddresses(Long userId);
    Address addAddress(Long userId, AddressRequest request);
    Address updateAddress(Long userId, Long addressId, AddressRequest request);
    void deleteAddress(Long userId, Long addressId);
    void setDefaultAddress(Long userId, Long addressId);
    
    // Order Management
    Page<Order> getOrders(Long userId, Pageable pageable);
    Order getOrderDetails(Long userId, Long orderId);
    void cancelOrder(Long userId, Long orderId);
    void requestRefund(Long userId, Long orderId, String reason);
    
    // Favorites
    void addToFavorites(Long userId, Long productId);
    void removeFromFavorites(Long userId, Long productId);
    Page<Product> getFavorites(Long userId, Pageable pageable);
    
    // Reviews
    Review addReview(Long userId, ReviewRequest request);
    List<Review> getUserReviews(Long userId);
    void deleteReview(Long userId, Long reviewId);
    
    // Notifications
    void enableNotifications(Long userId, String notificationType);
    void disableNotifications(Long userId, String notificationType);
    List<String> getEnabledNotifications(Long userId);
} 