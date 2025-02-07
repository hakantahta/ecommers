package com.ecommerce.repository;

import com.ecommerce.model.User;
import com.ecommerce.model.UserRole;
import com.ecommerce.model.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Kullanıcıya özel sorgular eklenebilir
    Optional<User> findByEmail(String email);
    
    // Email'in veritabanında olup olmadığını kontrol eder
    boolean existsByEmail(String email);
    
    // Admin operations
    Page<User> findByRole(UserRole role, Pageable pageable);
    Page<User> findByRoleAndAccountStatus(UserRole role, AccountStatus status, Pageable pageable);
} 