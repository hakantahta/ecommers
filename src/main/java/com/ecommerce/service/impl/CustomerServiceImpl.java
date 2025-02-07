package com.ecommerce.service.impl;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.request.CustomerProfileUpdateRequest;
import com.ecommerce.dto.request.ReviewRequest;
import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import com.ecommerce.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateProfile(Long userId, CustomerProfileUpdateRequest request) {
        User user = getUserById(userId);
        // Update user profile fields
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<Address> getAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Address addAddress(Long userId, AddressRequest request) {
        User user = getUserById(userId);
        Address address = new Address();
        updateAddressFromRequest(address, request);
        address.setUser(user);
        
        if (request.isDefault() || addressRepository.countByUserId(userId) == 0) {
            addressRepository.resetDefaultAddress(userId);
            address.setDefault(true);
        }
        
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address updateAddress(Long userId, Long addressId, AddressRequest request) {
        Address address = getAddressByIdAndUserId(addressId, userId);
        updateAddressFromRequest(address, request);
        
        if (request.isDefault()) {
            addressRepository.resetDefaultAddress(userId);
            address.setDefault(true);
        }
        
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = getAddressByIdAndUserId(addressId, userId);
        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        addressRepository.resetDefaultAddress(userId);
        Address address = getAddressByIdAndUserId(addressId, userId);
        address.setDefault(true);
        addressRepository.save(address);
    }

    @Override
    public Page<Order> getOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Order getOrderDetails(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        validateOrderOwnership(userId, order);
        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = getOrderDetails(userId, orderId);
        if (!order.getStatus().equals("PENDING") && !order.getStatus().equals("CONFIRMED")) {
            throw new IllegalStateException("Order cannot be cancelled in its current state");
        }
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void requestRefund(Long userId, Long orderId, String reason) {
        Order order = getOrderDetails(userId, orderId);
        if (!order.getStatus().equals("DELIVERED")) {
            throw new IllegalStateException("Refund can only be requested for delivered orders");
        }
        order.setStatus("REFUND_REQUESTED");
        order.setCancellationReason(reason);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void addToFavorites(Long userId, Long productId) {
        User user = getUserById(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        user.getFavorites().add(product);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFromFavorites(Long userId, Long productId) {
        User user = getUserById(userId);
        user.getFavorites().removeIf(p -> p.getId().equals(productId));
        userRepository.save(user);
    }

    @Override
    public Page<Product> getFavorites(Long userId, Pageable pageable) {
        return productRepository.findByFavoriteUsers_Id(userId, pageable);
    }

    @Override
    @Transactional
    public Review addReview(Long userId, ReviewRequest request) {
        User user = getUserById(userId);
        Review review = new Review();
        review.setUser(user);
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());
        
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            review.setProduct(product);
        }
        
        if (request.getVendorId() != null) {
            User vendor = userRepository.findById(request.getVendorId())
                    .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));
            if (vendor.getRole() != UserRole.VENDOR) {
                throw new IllegalArgumentException("User is not a vendor");
            }
            review.setVendor(vendor);
        }
        
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        if (!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }
        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public void enableNotifications(Long userId, String notificationType) {
        User user = getUserById(userId);
        user.getEnabledNotifications().add(notificationType);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disableNotifications(Long userId, String notificationType) {
        User user = getUserById(userId);
        user.getEnabledNotifications().remove(notificationType);
        userRepository.save(user);
    }

    @Override
    public List<String> getEnabledNotifications(Long userId) {
        User user = getUserById(userId);
        return user.getEnabledNotifications();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Address getAddressByIdAndUserId(Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        if (!address.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only manage your own addresses");
        }
        return address;
    }

    private void validateOrderOwnership(Long userId, Order order) {
        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only access your own orders");
        }
    }

    private void updateAddressFromRequest(Address address, AddressRequest request) {
        address.setTitle(request.getTitle());
        address.setFullAddress(request.getFullAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setPhone(request.getPhone());
        address.setAdditionalInfo(request.getAdditionalInfo());
    }
} 