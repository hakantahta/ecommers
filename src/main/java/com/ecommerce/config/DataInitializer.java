package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Eğer admin kullanıcısı zaten varsa, verileri tekrar oluşturma
        if (userRepository.findByEmail("admin@example.com").isPresent()) {
            return;
        }

        // Admin kullanıcısı oluştur
        User admin = new User();
        admin.setName("Admin");
        admin.setSurname("User");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        admin.setIsVerified(true);
        admin.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(admin);

        // Satıcı kullanıcısı oluştur
        User vendor = new User();
        vendor.setName("Vendor");
        vendor.setSurname("User");
        vendor.setEmail("vendor@example.com");
        vendor.setPassword(passwordEncoder.encode("vendor123"));
        vendor.setRole(UserRole.VENDOR);
        vendor.setIsVerified(true);
        vendor.setAccountStatus(AccountStatus.ACTIVE);
        vendor.setStoreName("Test Store");
        vendor.setStoreDescription("This is a test store");
        vendor.setContactEmail("contact@teststore.com");
        vendor.setContactPhone("+901234567890");
        userRepository.save(vendor);

        // Vendor nesnesini oluştur
        Vendor vendorEntity = new Vendor();
        vendorEntity.setUser(vendor);
        vendorEntity.setStoreName(vendor.getStoreName());
        vendorEntity.setStoreDescription(vendor.getStoreDescription());
        vendorEntity.setContactEmail(vendor.getContactEmail());
        vendorEntity.setContactPhone(vendor.getContactPhone());
        vendorRepository.save(vendorEntity);

        // Normal kullanıcı oluştur
        User customer = new User();
        customer.setName("Customer");
        customer.setSurname("User");
        customer.setEmail("customer@example.com");
        customer.setPassword(passwordEncoder.encode("customer123"));
        customer.setRole(UserRole.CUSTOMER);
        customer.setIsVerified(true);
        customer.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(customer);

        // Müşteri için adres oluştur
        Address customerAddress = new Address();
        customerAddress.setUser(customer);
        customerAddress.setTitle("Home");
        customerAddress.setFullAddress("123 Test Street");
        customerAddress.setCity("Test City");
        customerAddress.setState("Test State");
        customerAddress.setCountry("Test Country");
        customerAddress.setPostalCode("12345");
        customerAddress.setPhone("+901234567890");
        customerAddress.setDefault(true);
        addressRepository.save(customerAddress);

        // Kategoriler oluştur
        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setIcon("electronics-icon");
        electronics.setType(CategoryType.PHYSICAL);
        categoryRepository.save(electronics);

        Category clothing = new Category();
        clothing.setName("Clothing");
        clothing.setIcon("clothing-icon");
        clothing.setType(CategoryType.PHYSICAL);
        categoryRepository.save(clothing);

        // Ürünler oluştur
        Product laptop = new Product();
        laptop.setName("Test Laptop");
        laptop.setDescription("This is a test laptop");
        laptop.setPrice(new BigDecimal("999.99"));
        laptop.setStock(10);
        laptop.setImageUrl("laptop.jpg");
        laptop.setCategory(electronics);
        laptop.setVendor(vendorEntity);
        laptop.setStatus(ProductStatus.ACTIVE);
        productRepository.save(laptop);

        Product smartphone = new Product();
        smartphone.setName("Test Smartphone");
        smartphone.setDescription("This is a test smartphone");
        smartphone.setPrice(new BigDecimal("499.99"));
        smartphone.setStock(20);
        smartphone.setImageUrl("smartphone.jpg");
        smartphone.setCategory(electronics);
        smartphone.setVendor(vendorEntity);
        smartphone.setStatus(ProductStatus.ACTIVE);
        productRepository.save(smartphone);

        Product tshirt = new Product();
        tshirt.setName("Test T-Shirt");
        tshirt.setDescription("This is a test t-shirt");
        tshirt.setPrice(new BigDecimal("29.99"));
        tshirt.setStock(100);
        tshirt.setImageUrl("tshirt.jpg");
        tshirt.setCategory(clothing);
        tshirt.setVendor(vendorEntity);
        tshirt.setStatus(ProductStatus.ACTIVE);
        productRepository.save(tshirt);

        // Kullanıcının favori ürünlerini ayarla
        customer.setFavorites(new HashSet<>(Arrays.asList(laptop, smartphone)));
        userRepository.save(customer);

        // Sipariş oluştur
        Order order = new Order();
        order.setUser(customer);
        order.setStatus("COMPLETED");
        order.setTotalPrice(new BigDecimal("1529.97")); // laptop + smartphone
        order.setShippingAddress(customerAddress);
        order.setBillingAddress(customerAddress);
        order.setPaymentMethod("CREDIT_CARD");
        order.setShippingMethod("STANDARD_DELIVERY");
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
} 