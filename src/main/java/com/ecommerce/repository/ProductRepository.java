package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);
    
    Page<Product> findByFavoriteUsers_Id(Long userId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> search(String searchTerm);

    @Modifying
    @Query("UPDATE Product p SET p.status = 'APPROVED' WHERE p.id = :productId")
    void approveProduct(Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.status = 'REJECTED', p.rejectionReason = :reason WHERE p.id = :productId")
    void rejectProduct(Long productId, String reason);
} 