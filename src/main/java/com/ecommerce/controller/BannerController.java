package com.ecommerce.controller;

import com.ecommerce.dto.BannerDTO;
import com.ecommerce.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
public class BannerController {
    private final BannerService bannerService;

    @Autowired
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/main")
    public ResponseEntity<List<BannerDTO>> getMainBanners() {
        return ResponseEntity.ok(bannerService.getMainBanners());
    }

    @GetMapping("/side")
    public ResponseEntity<List<BannerDTO>> getSideBanners() {
        return ResponseEntity.ok(bannerService.getSideBanners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerDTO> getBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(bannerService.getBannerById(id));
    }

    @PostMapping
    public ResponseEntity<BannerDTO> createBanner(@RequestBody BannerDTO bannerDTO) {
        return ResponseEntity.ok(bannerService.createBanner(bannerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerDTO> updateBanner(@PathVariable Long id, @RequestBody BannerDTO bannerDTO) {
        return ResponseEntity.ok(bannerService.updateBanner(id, bannerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/order")
    public ResponseEntity<Void> updateBannerOrder(@PathVariable Long id, @RequestParam Integer newOrder) {
        bannerService.updateBannerOrder(id, newOrder);
        return ResponseEntity.ok().build();
    }
} 