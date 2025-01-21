package com.ecommerce.service.impl;

import com.ecommerce.model.Favorite;
import com.ecommerce.repository.FavoriteRepository;
import com.ecommerce.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
} 