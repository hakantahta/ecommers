package com.ecommerce.controller;

import com.ecommerce.dto.request.OrderRequestDto;
import com.ecommerce.dto.response.OrderResponseDto;
import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        // DTO dönüşümü yapılabilir
        return ResponseEntity.ok(orders.stream().map(this::convertToResponseDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(convertToResponseDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUserId(orderRequestDto.getUserId());
        order.setStatus(orderRequestDto.getStatus());
        order.setTotalPrice(orderRequestDto.getTotalPrice());
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(convertToResponseDto(createdOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private OrderResponseDto convertToResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        return dto;
    }
} 