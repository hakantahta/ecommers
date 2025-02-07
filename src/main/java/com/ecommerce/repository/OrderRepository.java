package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    List<Order> findByUserId(Long userId);
    
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.product.vendor.id = :vendorId")
    Page<Order> findByVendorId(Long vendorId, Pageable pageable);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = 'COMPLETED'")
    Double calculateTotalRevenue();

    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED', o.cancellationReason = :reason WHERE o.id = :orderId")
    void cancelOrder(Long orderId, String reason);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'REFUNDED' WHERE o.id = :orderId")
    void refundOrder(Long orderId);
} 