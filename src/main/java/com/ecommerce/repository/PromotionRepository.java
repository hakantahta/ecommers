package com.ecommerce.repository;

import com.ecommerce.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Page<Promotion> findByVendorId(Long vendorId, Pageable pageable);
    
    List<Promotion> findByVendorIdAndIsActiveTrue(Long vendorId);
    
    List<Promotion> findByIsActiveTrueAndIsApprovedTrueAndStartDateBeforeAndEndDateAfter(
        LocalDateTime now, LocalDateTime now2);
        
    boolean existsByVendorIdAndCouponCode(Long vendorId, String couponCode);
} 