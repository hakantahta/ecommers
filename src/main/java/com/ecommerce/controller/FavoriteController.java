package com.ecommerce.controller;

import com.ecommerce.dto.response.FavoriteResponseDto;
import com.ecommerce.dto.response.ProductResponseDto;
import com.ecommerce.model.Favorite;
import com.ecommerce.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteResponseDto>> getFavorites(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        List<FavoriteResponseDto> responseDtos = favorites.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping
    public ResponseEntity<FavoriteResponseDto> addFavorite(@RequestBody Favorite favorite) {
        Favorite createdFavorite = favoriteService.addFavorite(favorite);
        return ResponseEntity.ok(convertToResponseDto(createdFavorite));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        favoriteService.removeFavorite(id);
        return ResponseEntity.noContent().build();
    }

    private FavoriteResponseDto convertToResponseDto(Favorite favorite) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUser().getId());
        
        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setId(favorite.getProduct().getId());
        productDto.setName(favorite.getProduct().getName());
        productDto.setDescription(favorite.getProduct().getDescription());
        productDto.setPrice(favorite.getProduct().getPrice());
        productDto.setStock(favorite.getProduct().getStock());
        productDto.setActive(favorite.getProduct().getActive());
        
        dto.setProduct(productDto);
        return dto;
    }
} 
