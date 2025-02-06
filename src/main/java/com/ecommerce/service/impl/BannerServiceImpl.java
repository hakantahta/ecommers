package com.ecommerce.service.impl;

import com.ecommerce.model.Banner;
import com.ecommerce.repository.BannerRepository;
import com.ecommerce.service.BannerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @Override
    public List<Banner> getActiveBanners() {
        return bannerRepository.findByActiveOrderByDisplayOrderAsc(true);
    }

    @Override
    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found with id: " + id));
    }

    @Override
    @Transactional
    public Banner createBanner(Banner banner) {
        if (banner.getDisplayOrder() == null) {
            banner.setDisplayOrder(bannerRepository.count() > 0 ? 
                (int) bannerRepository.count() + 1 : 1);
        }
        return bannerRepository.save(banner);
    }

    @Override
    @Transactional
    public Banner updateBanner(Long id, Banner banner) {
        Banner existingBanner = getBannerById(id);
        existingBanner.setTitle(banner.getTitle());
        existingBanner.setImageUrl(banner.getImageUrl());
        existingBanner.setLink(banner.getLink());
        existingBanner.setDisplayOrder(banner.getDisplayOrder());
        existingBanner.setActive(banner.getActive());
        existingBanner.setDescription(banner.getDescription());
        return bannerRepository.save(existingBanner);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateBannerOrder(Long id, Integer newOrder) {
        Banner banner = getBannerById(id);
        banner.setDisplayOrder(newOrder);
        bannerRepository.save(banner);
    }
} 