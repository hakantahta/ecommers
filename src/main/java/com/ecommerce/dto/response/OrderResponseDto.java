package com.ecommerce.dto.response;

import com.ecommerce.model.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Long addressId;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long productId;
        private Integer quantity;
        private BigDecimal price;
    }
} 