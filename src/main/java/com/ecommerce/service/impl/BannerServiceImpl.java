package com.ecommerce.service.impl;

import com.ecommerce.dto.BannerDTO;
import com.ecommerce.model.Banner;
import com.ecommerce.model.Banner.BannerType;
import com.ecommerce.repository.BannerRepository;
import com.ecommerce.service.BannerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public List<BannerDTO> getMainBanners() {
        return bannerRepository.findByTypeAndIsActiveTrueOrderByDisplayOrderAsc(BannerType.MAIN)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BannerDTO> getSideBanners() {
        return bannerRepository.findByTypeAndIsActiveTrueOrderByDisplayOrderAsc(BannerType.SIDE)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BannerDTO getBannerById(Long id) {
        return convertToDTO(bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found with id: " + id)));
    }

    @Override
    @Transactional
    public BannerDTO createBanner(BannerDTO bannerDTO) {
        Banner banner = convertToEntity(bannerDTO);
        if (banner.getDisplayOrder() == null) {
            banner.setDisplayOrder(bannerRepository.count() > 0 ? 
                (int) bannerRepository.count() + 1 : 1);
        }
        return convertToDTO(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public BannerDTO updateBanner(Long id, BannerDTO bannerDTO) {
        Banner existingBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found with id: " + id));
        
        existingBanner.setTitle(bannerDTO.getTitle());
        existingBanner.setImageUrl(bannerDTO.getImageUrl());
        existingBanner.setLink(bannerDTO.getLink());
        existingBanner.setType(bannerDTO.getType());
        existingBanner.setDisplayOrder(bannerDTO.getDisplayOrder());
        existingBanner.setIsActive(bannerDTO.getIsActive());
        
        return convertToDTO(bannerRepository.save(existingBanner));
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateBannerOrder(Long id, Integer newOrder) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found with id: " + id));
        banner.setDisplayOrder(newOrder);
        bannerRepository.save(banner);
    }

    private BannerDTO convertToDTO(Banner banner) {
        return new BannerDTO(
                banner.getId(),
                banner.getTitle(),
                banner.getImageUrl(),
                banner.getLink(),
                banner.getType(),
                banner.getDisplayOrder(),
                banner.getIsActive()
        );
    }

    private Banner convertToEntity(BannerDTO dto) {
        Banner banner = new Banner();
        banner.setTitle(dto.getTitle());
        banner.setImageUrl(dto.getImageUrl());
        banner.setLink(dto.getLink());
        banner.setType(dto.getType());
        banner.setDisplayOrder(dto.getDisplayOrder());
        banner.setIsActive(dto.getIsActive());
        return banner;
    }
} 