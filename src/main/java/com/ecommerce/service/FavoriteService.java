package com.ecommerce.service;

import com.ecommerce.model.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getFavoritesByUserId(Long userId);
    Favorite addFavorite(Favorite favorite);
    void removeFavorite(Long id);
} 