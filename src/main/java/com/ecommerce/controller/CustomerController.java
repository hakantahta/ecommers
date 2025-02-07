package com.ecommerce.controller;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.request.CustomerProfileUpdateRequest;
import com.ecommerce.dto.request.ReviewRequest;
import com.ecommerce.model.Address;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@Tag(name = "Customer", description = "Customer operations API")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;

    // Profile Management
    @PatchMapping("/profile")
    @Operation(summary = "Update customer profile")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CustomerProfileUpdateRequest request) {
        customerService.updateProfile(getUserId(userDetails), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        customerService.changePassword(getUserId(userDetails), oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    // Address Management
    @GetMapping("/addresses")
    @Operation(summary = "Get all addresses")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerService.getAddresses(getUserId(userDetails)));
    }

    @PostMapping("/addresses")
    @Operation(summary = "Add new address")
    public ResponseEntity<Address> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(customerService.addAddress(getUserId(userDetails), request));
    }

    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "Update address")
    public ResponseEntity<Address> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(customerService.updateAddress(getUserId(userDetails), addressId, request));
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "Delete address")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        customerService.deleteAddress(getUserId(userDetails), addressId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/addresses/{addressId}/default")
    @Operation(summary = "Set address as default")
    public ResponseEntity<Void> setDefaultAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        customerService.setDefaultAddress(getUserId(userDetails), addressId);
        return ResponseEntity.ok().build();
    }

    // Order Management
    @GetMapping("/orders")
    @Operation(summary = "Get all orders")
    public ResponseEntity<Page<Order>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(customerService.getOrders(getUserId(userDetails), pageable));
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Get order details")
    public ResponseEntity<Order> getOrderDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(customerService.getOrderDetails(getUserId(userDetails), orderId));
    }

    @PostMapping("/orders/{orderId}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        customerService.cancelOrder(getUserId(userDetails), orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders/{orderId}/refund")
    @Operation(summary = "Request refund")
    public ResponseEntity<Void> requestRefund(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId,
            @RequestBody String reason) {
        customerService.requestRefund(getUserId(userDetails), orderId, reason);
        return ResponseEntity.ok().build();
    }

    // Favorites
    @PostMapping("/favorites/{productId}")
    @Operation(summary = "Add product to favorites")
    public ResponseEntity<Void> addToFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        customerService.addToFavorites(getUserId(userDetails), productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{productId}")
    @Operation(summary = "Remove product from favorites")
    public ResponseEntity<Void> removeFromFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        customerService.removeFromFavorites(getUserId(userDetails), productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get favorite products")
    public ResponseEntity<Page<Product>> getFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(customerService.getFavorites(getUserId(userDetails), pageable));
    }

    // Reviews
    @PostMapping("/reviews")
    @Operation(summary = "Add review")
    public ResponseEntity<Review> addReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(customerService.addReview(getUserId(userDetails), request));
    }

    @GetMapping("/reviews")
    @Operation(summary = "Get user reviews")
    public ResponseEntity<List<Review>> getUserReviews(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerService.getUserReviews(getUserId(userDetails)));
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Operation(summary = "Delete review")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId) {
        customerService.deleteReview(getUserId(userDetails), reviewId);
        return ResponseEntity.ok().build();
    }

    // Notifications
    @PostMapping("/notifications/{type}/enable")
    @Operation(summary = "Enable notification type")
    public ResponseEntity<Void> enableNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String type) {
        customerService.enableNotifications(getUserId(userDetails), type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/{type}/disable")
    @Operation(summary = "Disable notification type")
    public ResponseEntity<Void> disableNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String type) {
        customerService.disableNotifications(getUserId(userDetails), type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    @Operation(summary = "Get enabled notifications")
    public ResponseEntity<List<String>> getEnabledNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerService.getEnabledNotifications(getUserId(userDetails)));
    }

    private Long getUserId(UserDetails userDetails) {
        return ((User) userDetails).getId();
    }
} 