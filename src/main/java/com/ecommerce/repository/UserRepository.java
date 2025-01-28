package com.ecommerce.repository;

import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Kullanıcıya özel sorgular eklenebilir
    Optional<User> findByUsername(String username);
    
    // Kullanıcı adının veritabanında olup olmadığını kontrol eder
    boolean existsByUsername(String username);
    
    // Email'in veritabanında olup olmadığını kontrol eder
    boolean existsByEmail(String email);
} 