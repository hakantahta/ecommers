package com.ecommerce.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Long shippingAddressId;
    private Long billingAddressId;
    private String status;
    private BigDecimal totalPrice;
    private String shippingMethod;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;

    @Data
    @NoArgsConstructor
    public static class OrderItemDto {
        private Long productId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
} 