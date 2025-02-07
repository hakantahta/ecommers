package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorProfileUpdateRequest {
    @NotBlank(message = "Store name is required")
    private String storeName;
    
    @NotBlank(message = "Store description is required")
    private String storeDescription;
    
    private String logo;
    private String bannerImage;
    private String contactEmail;
    private String contactPhone;
    private String taxNumber;
    private String companyName;
    private String bankAccount;
} 