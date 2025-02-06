package com.ecommerce.service;

import com.ecommerce.model.Banner;
import java.util.List;

public interface BannerService {
    List<Banner> getAllBanners();
    List<Banner> getActiveBanners();
    Banner getBannerById(Long id);
    Banner createBanner(Banner banner);
    Banner updateBanner(Long id, Banner banner);
    void deleteBanner(Long id);
    void updateBannerOrder(Long id, Integer newOrder);
} 