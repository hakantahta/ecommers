package com.ecommerce.repository;

import com.ecommerce.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    // Temel CRUD operasyonları JpaRepository tarafından sağlanıyor
} 