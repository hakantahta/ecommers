package com.ecommerce.service;

import com.ecommerce.dto.request.CartItemRequestDto;
import com.ecommerce.dto.response.CartItemResponseDto;
import java.util.List;

public interface CartService {
    List<CartItemResponseDto> getCartItemsByUserId(Long userId);
    CartItemResponseDto addCartItem(CartItemRequestDto cartItemRequestDto);
    void removeCartItem(Long id);
    CartItemResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto);
}
