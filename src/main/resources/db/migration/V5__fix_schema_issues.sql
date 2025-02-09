-- First, ensure the default vendor exists and is valid
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM users 
        WHERE email = 'system@vendor.com' AND role = 'VENDOR'
    ) THEN
        INSERT INTO users (email, password, role, is_verified, account_status)
        VALUES ('system@vendor.com', '$2a$10$default_password_hash', 'VENDOR', true, 'ACTIVE');
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM vendors v
        INNER JOIN users u ON v.user_id = u.id
        WHERE u.email = 'system@vendor.com'
    ) THEN
        INSERT INTO vendors (user_id, store_name, contact_email, contact_phone, rating, review_count, total_orders, is_active)
        SELECT id, 'System Vendor', 'system@vendor.com', '0000000000', 0.0, 0, 0, true
        FROM users
        WHERE email = 'system@vendor.com';
    END IF;
END $$;

-- Fix products table issues
UPDATE products 
SET 
    image_url = COALESCE(image_url, 'default-product-image.jpg'),
    is_active = COALESCE(is_active, true),
    is_featured = COALESCE(is_featured, false),
    order_count = COALESCE(order_count, 0),
    rating = COALESCE(rating, 0.0),
    review_count = COALESCE(review_count, 0),
    stock = COALESCE(stock, 0),
    vendor_id = CASE 
        WHEN vendor_id IS NULL OR NOT EXISTS (SELECT 1 FROM vendors WHERE id = vendor_id)
        THEN (SELECT id FROM vendors v INNER JOIN users u ON v.user_id = u.id WHERE u.email = 'system@vendor.com')
        ELSE vendor_id
    END;

-- Now safely add NOT NULL constraints
ALTER TABLE products 
    ALTER COLUMN image_url SET NOT NULL,
    ALTER COLUMN is_active SET NOT NULL,
    ALTER COLUMN is_featured SET NOT NULL,
    ALTER COLUMN order_count SET NOT NULL,
    ALTER COLUMN rating SET NOT NULL,
    ALTER COLUMN review_count SET NOT NULL,
    ALTER COLUMN stock SET NOT NULL,
    ALTER COLUMN vendor_id SET NOT NULL; 