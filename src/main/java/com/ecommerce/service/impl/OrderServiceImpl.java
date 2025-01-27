package com.ecommerce.service.impl;

import com.ecommerce.dto.request.OrderRequestDto;
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
    public OrderResponseDto createOrder(OrderRequestDto orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus("PENDING");
        order.setItems(new ArrayList<>());

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (var itemDto : orderRequest.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            order.getItems().add(orderItem);
            totalPrice = totalPrice.add(orderItem.getPrice());
        }

        order.setTotalPrice(totalPrice);
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
        dto.setAddressId(order.getAddress().getId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        
        dto.setItems(order.getItems().stream()
                .map(this::mapToOrderItemDto)
                .collect(Collectors.toList()));
        
        return dto;
    }

    private OrderResponseDto.OrderItemDto mapToOrderItemDto(OrderItem item) {
        OrderResponseDto.OrderItemDto dto = new OrderResponseDto.OrderItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
} 
