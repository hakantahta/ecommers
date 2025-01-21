package com.ecommerce.service.impl;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(User user) {
        // Kullanıcı kaydı için gerekli işlemler
        return userRepository.save(user);
    }

    @Override
    public String login(User user) {
        // Kullanıcı girişi için gerekli işlemler
        return "JWT_TOKEN"; // Örnek olarak bir JWT token döndürüyoruz
    }
} 