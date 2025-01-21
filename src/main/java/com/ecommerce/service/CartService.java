package com.ecommerce.service;

import com.ecommerce.dto.request.CartItemRequestDto;
import com.ecommerce.dto.response.CartItemResponseDto;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<CartItemResponseDto> getCartItems() {
        return cartItemRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public CartItemResponseDto addToCart(CartItemRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setUserId(requestDto.getUserId());
        cartItem.setProduct(product);
        cartItem.setQuantity(requestDto.getQuantity());

        CartItem savedItem = cartItemRepository.save(cartItem);
        return convertToResponseDto(savedItem);
    }

    public void removeFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }

    public CartItemResponseDto updateCartItem(Long id, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(requestDto.getQuantity());
        CartItem updatedItem = cartItemRepository.save(cartItem);
        return convertToResponseDto(updatedItem);
    }

    private CartItemResponseDto convertToResponseDto(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(cartItem.getId());
        dto.setUserId(cartItem.getUserId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getProduct().getPrice());
        dto.setTotalPrice(cartItem.getProduct().getPrice()
            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return dto;
    }

    public abstract List<CartItem> getCartItemsByUserId(Long userId);

    public abstract CartItem addCartItem(CartItem cartItem);

    public abstract void removeCartItem(Long id);
}
