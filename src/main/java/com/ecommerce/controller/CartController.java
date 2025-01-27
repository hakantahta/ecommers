package com.ecommerce.controller;

import com.ecommerce.dto.request.CartItemRequestDto;
import com.ecommerce.dto.response.CartItemResponseDto;
import com.ecommerce.model.CartItem;
import com.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getCartItems() {
        return ResponseEntity.ok(cartService.getCartItems());
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponseDto> addToCart(@RequestBody CartItemRequestDto cartItemRequestDto) {
        return ResponseEntity.ok(cartService.addToCart(cartItemRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDto> updateCartItem(@PathVariable Long id, 
                                                            @RequestBody CartItemRequestDto cartItemRequestDto) {
        return ResponseEntity.ok(cartService.updateCartItem(id, cartItemRequestDto));
    }

    private CartItemResponseDto convertToResponseDto(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(cartItem.getId());
        dto.setUserId(cartItem.getUser().getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        dto.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        return dto;
    }
} 
