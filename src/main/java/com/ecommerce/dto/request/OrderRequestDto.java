package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Address ID is required")
    private Long addressId;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequestDto> items;
    
    @NotNull(message = "Shipping method is required")
    private String shippingMethod;
    
    @NotNull(message = "Payment method is required")
    private String paymentMethod;
    
    @NotNull(message = "Order status is required")
    private String status;

    public OrderRequestDto() {
        this.shippingMethod = "STANDARD";
        this.paymentMethod = "CREDIT_CARD";
        this.status = "PENDING";
    }
} 