package com.ecommerce.repository;

import com.ecommerce.model.Banner;
import com.ecommerce.model.Banner.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByTypeAndIsActiveTrueOrderByDisplayOrderAsc(BannerType type);
} 