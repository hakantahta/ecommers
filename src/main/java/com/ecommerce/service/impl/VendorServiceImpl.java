package com.ecommerce.service.impl;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.VendorDTO;
import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.request.PromotionRequest;
import com.ecommerce.dto.request.VendorProfileUpdateRequest;
import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import com.ecommerce.service.VendorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PromotionRepository promotionRepository;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        User user = userRepository.findById(vendorDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Önce vendor'ı oluştur
        Vendor vendor = new Vendor();
        vendor.setUser(user);
        vendor.setStoreName(vendorDTO.getStoreName());
        vendor.setStoreDescription(vendorDTO.getStoreDescription());
        vendor.setContactEmail(vendorDTO.getContactEmail());
        vendor.setContactPhone(vendorDTO.getContactPhone());

        return convertToDTO(vendorRepository.save(vendor));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(Long vendorId, ProductDTO productDTO) {
        // Önce vendor'ı bul
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));

        // Kategoriyi bul
        Category category = null;
        if (productDTO.getCategoryId() != null) {
            category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        // Ürünü oluştur
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);
        product.setVendor(vendor);
        product.setStatus(ProductStatus.PENDING);
        product.setIsActive(true);

        return convertToProductDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long vendorId, Long productId, ProductDTO productDTO) {
        Product product = getProductByIdAndVendorId(productId, vendorId);

        // Kategoriyi güncelle
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            product.setCategory(category);
        }

        // Diğer alanları güncelle
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setStatus(ProductStatus.PENDING);

        return convertToProductDTO(productRepository.save(product));
    }

    @Override
    public void updateProfile(Long vendorId, VendorProfileUpdateRequest request) {

    }

    @Override
    public Product addProduct(Long vendorId, ProductRequest request) {
        return null;
    }

    @Override
    public Product updateProduct(Long vendorId, Long productId, ProductRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long vendorId, Long productId) {
        Product product = getProductByIdAndVendorId(productId, vendorId);
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProductStock(Long vendorId, Long productId, Integer quantity) {
        Product product = getProductByIdAndVendorId(productId, vendorId);
        product.setStock(quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProductStatus(Long vendorId, Long productId, String status) {
        Product product = getProductByIdAndVendorId(productId, vendorId);
        product.setStatus(ProductStatus.valueOf(status));
        productRepository.save(product);
    }

    @Override
    public Page<Product> getVendorProducts(Long vendorId, Pageable pageable) {
        return productRepository.findByVendorId(vendorId, pageable);
    }

    @Override
    public Page<Order> getVendorOrders(Long vendorId, Pageable pageable) {
        return orderRepository.findByVendorId(vendorId, pageable);
    }

    @Override
    public Order getOrderDetails(Long vendorId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        validateOrderOwnership(vendorId, order);
        return order;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long vendorId, Long orderId, String status) {
        Order order = getOrderDetails(vendorId, orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void processRefundRequest(Long vendorId, Long orderId, boolean approved, String reason) {
        Order order = getOrderDetails(vendorId, orderId);
        if (!order.getStatus().equals("REFUND_REQUESTED")) {
            throw new IllegalStateException("Order is not in refund requested state");
        }
        order.setStatus(approved ? "REFUNDED" : "REFUND_REJECTED");
        order.setCancellationReason(reason);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Promotion createPromotion(Long vendorId, PromotionRequest request) {
        User vendor = getVendorById(vendorId);
        
        if (request.getCouponCode() != null && 
            promotionRepository.existsByVendorIdAndCouponCode(vendorId, request.getCouponCode())) {
            throw new IllegalArgumentException("Coupon code already exists");
        }

        Promotion promotion = new Promotion();
        updatePromotionFromRequest(promotion, request);
        promotion.setVendor(vendor);
        
        if (request.getProductIds() != null) {
            List<Product> products = productRepository.findAllById(request.getProductIds());
            promotion.getProducts().addAll(products);
        }

        return promotionRepository.save(promotion);
    }

    @Override
    @Transactional
    public Promotion updatePromotion(Long vendorId, Long promotionId, PromotionRequest request) {
        Promotion promotion = getPromotionByIdAndVendorId(promotionId, vendorId);
        updatePromotionFromRequest(promotion, request);
        
        if (request.getProductIds() != null) {
            promotion.getProducts().clear();
            List<Product> products = productRepository.findAllById(request.getProductIds());
            promotion.getProducts().addAll(products);
        }

        return promotionRepository.save(promotion);
    }

    @Override
    @Transactional
    public void deletePromotion(Long vendorId, Long promotionId) {
        Promotion promotion = getPromotionByIdAndVendorId(promotionId, vendorId);
        promotion.setIsActive(false);
        promotionRepository.save(promotion);
    }

    @Override
    public Page<Promotion> getVendorPromotions(Long vendorId, Pageable pageable) {
        return promotionRepository.findByVendorId(vendorId, pageable);
    }

    @Override
    public List<Review> getProductReviews(Long vendorId, Long productId) {
        Product product = getProductByIdAndVendorId(productId, vendorId);
        return reviewRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public void respondToReview(Long vendorId, Long reviewId, String response) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        validateReviewOwnership(vendorId, review);
        // Add vendor response logic here
    }

    @Override
    public Map<String, BigDecimal> getRevenueStats(Long vendorId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement revenue statistics calculation
        return new HashMap<>();
    }

    @Override
    public Map<String, Long> getOrderStats(Long vendorId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement order statistics calculation
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getProductStats(Long vendorId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement product statistics calculation
        return new HashMap<>();
    }

    @Override
    public Map<String, BigDecimal> getFinancialSummary(Long vendorId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement financial summary calculation
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getTransactionHistory(Long vendorId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implement transaction history retrieval
        return List.of();
    }

    private User getVendorById(Long vendorId) {
        User vendor = userRepository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));
        if (vendor.getRole() != UserRole.VENDOR) {
            throw new AccessDeniedException("User is not a vendor");
        }
        return vendor;
    }

    private Product getProductByIdAndVendorId(Long productId, Long vendorId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (!product.getVendor().getId().equals(vendorId)) {
            throw new IllegalStateException("Product does not belong to this vendor");
        }

        return product;
    }

    private Promotion getPromotionByIdAndVendorId(Long promotionId, Long vendorId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found"));
        if (!promotion.getVendor().getId().equals(vendorId)) {
            throw new AccessDeniedException("Promotion does not belong to vendor");
        }
        return promotion;
    }

    private void validateOrderOwnership(Long vendorId, Order order) {
        boolean isVendorOrder = order.getOrderItems().stream()
            .anyMatch(item -> item.getProduct().getVendor().getId().equals(vendorId));
        
        if (!isVendorOrder) {
            throw new AccessDeniedException("Order does not belong to vendor");
        }
    }

    private void validateReviewOwnership(Long vendorId, Review review) {
        if (!review.getProduct().getVendor().getId().equals(vendorId)) {
            throw new AccessDeniedException("Review does not belong to vendor's product");
        }
    }

    private void updateProductFromRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStockQuantity());
        // Ana resmi images listesinden al (eğer varsa)
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            product.setImageUrl(request.getImages().get(0));
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            product.setCategory(category);
        }
        product.setIsActive(request.getIsActive());
    }

    private void updatePromotionFromRequest(Promotion promotion, PromotionRequest request) {
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setDiscountType(request.getDiscountType());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setUsageLimit(request.getUsageLimit());
        promotion.setMinimumPurchaseAmount(request.getMinimumPurchaseAmount());
        promotion.setCouponCode(request.getCouponCode());
        promotion.setRequiresAdminApproval(request.getRequiresAdminApproval());
    }

    private VendorDTO convertToDTO(Vendor vendor) {
        return VendorDTO.builder()
                .id(vendor.getId())
                .userId(vendor.getUser().getId())
                .storeName(vendor.getStoreName())
                .storeDescription(vendor.getStoreDescription())
                .contactEmail(vendor.getContactEmail())
                .contactPhone(vendor.getContactPhone())
                .rating(vendor.getRating())
                .reviewCount(vendor.getReviewCount())
                .totalOrders(vendor.getTotalOrders())
                .isActive(vendor.getIsActive())
                .build();
    }

    private ProductDTO convertToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .status(product.getStatus())
                .vendorId(product.getVendor().getId())
                .isActive(product.getIsActive())
                .build();
    }
} 
