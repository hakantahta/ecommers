package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Full address is required")
    private String fullAddress;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State/Province is required")
    private String state;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotBlank(message = "Postal code is required")
    private String postalCode;
    
    private boolean isDefault;
    private String phone;
    private String additionalInfo;
} 