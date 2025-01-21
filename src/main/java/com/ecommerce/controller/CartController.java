package com.ecommerce.controller;

import com.ecommerce.dto.request.CartItemRequestDto;
import com.ecommerce.dto.response.CartItemResponseDto;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems.stream().map(this::convertToResponseDto).toList());
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDto> addCartItem(@RequestBody CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setUserId(cartItemRequestDto.getUserId());
        Product product = productRepository.findById(cartItemRequestDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        CartItem createdCartItem = cartService.addCartItem(cartItem);
        return ResponseEntity.ok(convertToResponseDto(createdCartItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        cartService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

    private CartItemResponseDto convertToResponseDto(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(cartItem.getId());
        dto.setUserId(cartItem.getUserId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
} 
