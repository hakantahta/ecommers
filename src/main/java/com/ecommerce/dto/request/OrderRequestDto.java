package com.ecommerce.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {
    private Long userId;
    private Long addressId;
    private List<OrderItemRequestDto> items;
} 