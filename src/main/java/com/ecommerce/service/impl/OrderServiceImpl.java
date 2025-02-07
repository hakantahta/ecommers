package com.ecommerce.service.impl;

import com.ecommerce.dto.request.OrderRequestDto;
import com.ecommerce.dto.request.OrderItemRequestDto;
import com.ecommerce.dto.response.OrderResponseDto;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.*;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return List.of();
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Address address = addressRepository.findById(orderRequestDto.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Order order = Order.builder()
                .user(user)
                .shippingAddress(address)
                .billingAddress(address)
                .shippingMethod(orderRequestDto.getShippingMethod() == null ? "STANDARD" : orderRequestDto.getShippingMethod())
                .paymentMethod(orderRequestDto.getPaymentMethod() == null ? "CREDIT_CARD" : orderRequestDto.getPaymentMethod())
                .status(orderRequestDto.getStatus() == null ? "PENDING" : orderRequestDto.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : orderRequestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponseDto(savedOrder);
    }

    @Override
    public void deleteOrder(Long id) {

    }

    @Override
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToOrderResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return mapToOrderResponseDto(orderRepository.save(order));
    }

    private OrderResponseDto mapToOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setShippingAddressId(order.getShippingAddress().getId());
        dto.setBillingAddressId(order.getBillingAddress().getId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setShippingMethod(order.getShippingMethod());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        
        dto.setItems(order.getOrderItems().stream()
                .map(this::mapToOrderItemDto)
                .collect(Collectors.toList()));
        
        return dto;
    }

    private OrderResponseDto.OrderItemDto mapToOrderItemDto(OrderItem item) {
        OrderResponseDto.OrderItemDto dto = new OrderResponseDto.OrderItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
} 
