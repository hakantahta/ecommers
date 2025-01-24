package com.ecommerce.service;

import com.ecommerce.dto.request.CartItemRequestDto;
import com.ecommerce.dto.response.CartItemResponseDto;
import java.util.List;

public interface CartService {
    List<CartItemResponseDto> getCartItems();
    CartItemResponseDto addToCart(CartItemRequestDto cartItemRequestDto);
    void removeFromCart(Long id);
    CartItemResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto);
}
