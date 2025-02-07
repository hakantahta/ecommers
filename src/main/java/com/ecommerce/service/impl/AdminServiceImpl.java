package com.ecommerce.service.impl;

import com.ecommerce.dto.response.UserResponseDto;
import com.ecommerce.model.AccountStatus;
import com.ecommerce.model.User;
import com.ecommerce.model.UserRole;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToUserResponseDto);
    }

    @Override
    public Page<UserResponseDto> getUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable)
                .map(this::convertToUserResponseDto);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, AccountStatus status) {
        User user = getUserById(userId);
        user.setAccountStatus(status);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void banUser(Long userId, String reason) {
        User user = getUserById(userId);
        user.setAccountStatus(AccountStatus.SUSPENDED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unbanUser(Long userId) {
        User user = getUserById(userId);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        user.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, UserRole newRole) {
        User user = getUserById(userId);
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public double getTotalRevenue() {
        return orderRepository.calculateTotalRevenue();
    }

    @Override
    @Transactional
    public void approveVendor(Long userId) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.VENDOR) {
            throw new IllegalStateException("User is not a vendor");
        }
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void rejectVendor(Long userId, String reason) {
        User user = getUserById(userId);
        if (user.getRole() != UserRole.VENDOR) {
            throw new IllegalStateException("User is not a vendor");
        }
        user.setAccountStatus(AccountStatus.SUSPENDED);
        user.setBanReason(reason);
        userRepository.save(user);
    }

    @Override
    public Page<UserResponseDto> getPendingVendors(Pageable pageable) {
        return userRepository.findByRoleAndAccountStatus(UserRole.VENDOR, AccountStatus.PENDING, pageable)
                .map(this::convertToUserResponseDto);
    }

    @Override
    @Transactional
    public void approveProduct(Long productId) {
        productRepository.approveProduct(productId);
    }

    @Override
    @Transactional
    public void rejectProduct(Long productId, String reason) {
        productRepository.rejectProduct(productId, reason);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        orderRepository.cancelOrder(orderId, reason);
    }

    @Override
    @Transactional
    public void refundOrder(Long orderId) {
        orderRepository.refundOrder(orderId);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .isBanned(user.isBanned())
                .banReason(user.getBanReason())
                .lastLoginDate(user.getLastLoginDate())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 