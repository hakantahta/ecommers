package com.ecommerce.service;

import com.ecommerce.dto.BannerDTO;
import java.util.List;

public interface BannerService {
    List<BannerDTO> getMainBanners();
    List<BannerDTO> getSideBanners();
    BannerDTO getBannerById(Long id);
    BannerDTO createBanner(BannerDTO bannerDTO);
    BannerDTO updateBanner(Long id, BannerDTO bannerDTO);
    void deleteBanner(Long id);
    void updateBannerOrder(Long id, Integer newOrder);
}