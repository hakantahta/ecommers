package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String fullAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String phone;
} 