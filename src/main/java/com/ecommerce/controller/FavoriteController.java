package com.ecommerce.controller;

import com.ecommerce.dto.response.FavoriteResponseDto;
import com.ecommerce.model.Favorite;
import com.ecommerce.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteResponseDto>> getFavorites(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        // DTO dönüşümü yapılabilir
        return ResponseEntity.ok(favorites.stream().map(this::convertToResponseDto).toList());
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
        dto.setUserId(favorite.getUserId());
        dto.setProduct(favorite.getProduct());
        return dto;
    }
} 