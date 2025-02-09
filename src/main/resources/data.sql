-- Reset sequences
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;
ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE orders_id_seq RESTART WITH 1;
ALTER SEQUENCE order_items_id_seq RESTART WITH 1;
ALTER SEQUENCE addresses_id_seq RESTART WITH 1;
ALTER SEQUENCE promotions_id_seq RESTART WITH 1;

-- Users (Admin, Customer, Vendor)
INSERT INTO users (name, surname, email, password, role, account_status, is_verified, created_at, updated_at)
VALUES 
('Admin', 'User', 'admin@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'ADMIN', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('John', 'Doe', 'john@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'CUSTOMER', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane', 'Smith', 'jane@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'CUSTOMER', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alice', 'Johnson', 'alice@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'CUSTOMER', 'PENDING', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob', 'Wilson', 'bob@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'CUSTOMER', 'SUSPENDED', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tech', 'Store', 'tech@store.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'VENDOR', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Fashion', 'Store', 'fashion@store.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'VENDOR', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Vendors (Önce vendors tablosuna veri ekliyoruz)
INSERT INTO vendors (user_id, store_name, store_description, contact_email, contact_phone, is_active, rating, review_count, total_orders, created_at)
VALUES 
(6, 'Tech Store', 'Best tech products in town', 'contact@techstore.com', '+90555555555', true, 0.0, 0, 0, CURRENT_TIMESTAMP),
(7, 'Fashion Store', 'Latest fashion trends', 'contact@fashionstore.com', '+90555555556', true, 0.0, 0, 0, CURRENT_TIMESTAMP);

-- Categories
INSERT INTO categories (name, icon, type)
VALUES 
('Electronics', 'electronics-icon.png', 'PHYSICAL'),
('Clothing', 'clothing-icon.png', 'PHYSICAL');

INSERT INTO categories (name, icon, type, parent_id)
VALUES 
('Smartphones', 'smartphone-icon.png', 'PHYSICAL', 1),
('Laptops', 'laptop-icon.png', 'PHYSICAL', 1),
('Men', 'men-icon.png', 'PHYSICAL', 2),
('Women', 'women-icon.png', 'PHYSICAL', 2);

-- Products (Vendors ve Categories ekledikten sonra Products ekliyoruz)
INSERT INTO products (name, description, price, stock, vendor_id, category_id, status, created_at, updated_at, is_active, is_featured, order_count, rating, review_count, image_url)
VALUES 
('Smartphone X', 'Latest smartphone with amazing features', 999.99, 50, 1, 3, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, false, 0, 0.0, 0, 'smartphone-x.jpg'),
('Laptop Pro', 'Professional laptop for work and gaming', 1499.99, 30, 1, 4, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, false, 0, 0.0, 0, 'laptop-pro.jpg'),
('Men''s Classic Suit', 'Elegant classic suit for men', 299.99, 20, 2, 5, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, false, 0, 0.0, 0, 'classic-suit.jpg'),
('Summer Dress', 'Beautiful summer dress', 79.99, 30, 2, 6, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, false, 0, 0.0, 0, 'summer-dress.jpg');

-- Addresses
INSERT INTO addresses (user_id, title, full_address, city, state, country, postal_code, phone, is_default)
VALUES 
(2, 'Home', '123 Main Street', 'Istanbul', 'Marmara', 'Turkey', '34000', '+905551234567', true),
(2, 'Office', '789 Business Center', 'Istanbul', 'Marmara', 'Turkey', '34001', '+905551234568', false),
(3, 'Home', '456 Park Avenue', 'Ankara', 'Central Anatolia', 'Turkey', '06000', '+905559876543', true);

-- Reviews
INSERT INTO reviews (user_id, product_id, rating, comment, created_at, updated_at)
VALUES 
(2, 1, 5, 'Great smartphone, very satisfied with the purchase!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 4, 'Good laptop for the price', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Orders
INSERT INTO orders (user_id, shipping_address_id, billing_address_id, status, shipping_method, payment_method, payment_status, total_price, tracking_number, created_at)
VALUES 
(2, 1, 1, 'DELIVERED', 'EXPRESS', 'CREDIT_CARD', 'PAID', 999.99, 'TN123456789', CURRENT_TIMESTAMP - INTERVAL '5 DAY'),
(3, 3, 3, 'PROCESSING', 'STANDARD', 'CREDIT_CARD', 'PAID', 1499.99, 'TN987654321', CURRENT_TIMESTAMP - INTERVAL '1 DAY');

-- Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, total_price)
VALUES 
(1, 1, 1, 999.99, 999.99),
(2, 2, 1, 1499.99, 1499.99);

-- Cart Items
INSERT INTO cart_items (user_id, product_id, quantity, price)
VALUES 
(2, 2, 1, 1499.99),
(3, 3, 2, 299.99);

-- Favorites
INSERT INTO favorites (user_id, product_id)
VALUES 
(2, 2),
(3, 3);

-- Promotions
INSERT INTO promotions (name, description, discount_type, discount_value, minimum_purchase_amount, usage_limit, start_date, end_date, created_at, updated_at, vendor_id, is_active, is_approved)
VALUES 
('First Purchase', 'Special discount for first-time buyers', 'PERCENTAGE', 25, 100.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 DAY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, true, true),
('Seasonal Sale', 'End of season sale', 'PERCENTAGE', 40, 200.00, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '15 DAY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, true, true);

-- Banners
INSERT INTO banners (title, image_url, link, type, display_order, is_active)
VALUES 
('Summer Sale', 'summer-sale-banner.jpg', '/sale/summer', 'MAIN', 1, true),
('New Arrivals', 'new-arrivals-banner.jpg', '/new-arrivals', 'SIDE', 2, true),
('Tech Week', 'tech-week-banner.jpg', '/sale/tech', 'MAIN', 3, true); 