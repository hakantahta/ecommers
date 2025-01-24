package com.ecommerce.service.impl;

import com.ecommerce.dto.request.CartItemRequestDto;
import com.ecommerce.dto.response.CartItemResponseDto;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CartItemResponseDto> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponseDto addCartItem(CartItemRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setUserId(requestDto.getUserId());
        cartItem.setProduct(product);
        cartItem.setQuantity(requestDto.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return convertToResponseDto(savedCartItem);
    }

    @Override
    public void removeCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItemResponseDto updateCartItem(Long id, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(requestDto.getQuantity());
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        return convertToResponseDto(updatedCartItem);
    }

    private CartItemResponseDto convertToResponseDto(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(cartItem.getId());
        dto.setUserId(cartItem.getUserId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getProduct().getPrice());
        dto.setTotalPrice(cartItem.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())));
        return dto;
    }
} 