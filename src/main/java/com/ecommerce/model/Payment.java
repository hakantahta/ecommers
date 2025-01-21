package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod; // Kredi Kartı, PayPal, vb.

    @Column(nullable = false)
    private String status; // Başarılı, Başarısız, Beklemede

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Diğer gerekli alanlar ve ilişkiler eklenebilir
} 