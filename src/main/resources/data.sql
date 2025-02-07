-- Test Users
INSERT INTO users (name, surname, email, password, role, account_status, is_verified) 
VALUES ('Test', 'User', 'test@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'USER', 'ACTIVE', true);

-- Test Addresses
INSERT INTO addresses (user_id, title, full_address, city, state, country, postal_code, phone, is_default) 
VALUES (1, 'Home', '123 Test Street', 'Test City', 'Test State', 'Test Country', '12345', '+1234567890', true);

-- Test Products
INSERT INTO products (name, description, price, stock, status) 
VALUES ('Test Product', 'Test Product Description', 99.99, 100, 'ACTIVE');

-- Test Orders
INSERT INTO orders (user_id, address_id, status, shipping_method, payment_method, payment_status, total_amount, created_at) 
VALUES (1, 1, 'PENDING', 'STANDARD', 'CREDIT_CARD', 'PENDING', 99.99, CURRENT_TIMESTAMP);

-- Test Order Items
INSERT INTO order_items (order_id, product_id, quantity, price) 
VALUES (1, 1, 1, 99.99); 