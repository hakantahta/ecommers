package com.ecommerce.service;

import com.ecommerce.model.CartItem;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItemsByUserId(Long userId);
    CartItem addCartItem(CartItem cartItem);
    void removeCartItem(Long id);
} 