package com.ecommerce.repository;

import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Kullanıcıya özel sorgular eklenebilir
    User findByEmail(String email);
} 