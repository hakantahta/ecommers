package com.ecommerce.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionRequest {
    @NotBlank(message = "Promotion name is required")
    private String name;

    @NotBlank(message = "Promotion description is required")
    private String description;

    @NotNull(message = "Discount value is required")
    @Min(value = 0, message = "Discount value must be greater than or equal to 0")
    private BigDecimal discountValue;

    @NotBlank(message = "Discount type is required")
    private String discountType; // PERCENTAGE, FIXED_AMOUNT

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    private List<Long> productIds;
    private Integer usageLimit;
    private BigDecimal minimumPurchaseAmount;
    private String couponCode;
    private Boolean requiresAdminApproval = true;
} 