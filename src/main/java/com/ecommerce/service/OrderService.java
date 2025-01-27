package com.ecommerce.service;

import com.ecommerce.dto.request.OrderRequestDto;
import com.ecommerce.dto.response.OrderResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllOrders();
    
    OrderResponseDto getOrderById(Long id);
    
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    
    void deleteOrder(Long id);

    OrderResponseDto getOrder(Long orderId);

    List<OrderResponseDto> getUserOrders(Long userId);

    @Transactional
    OrderResponseDto updateOrderStatus(Long orderId, String status);
}
