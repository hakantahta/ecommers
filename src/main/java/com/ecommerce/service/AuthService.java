package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        // Burada kullanıcı kaydı için gerekli işlemleri yapabilirsiniz
        return userRepository.save(user);
    }

    public String login(User user) {
        // Burada kullanıcı girişi için gerekli işlemleri yapabilirsiniz
        return "JWT_TOKEN"; // Örnek olarak bir JWT token döndürüyoruz
    }
} 