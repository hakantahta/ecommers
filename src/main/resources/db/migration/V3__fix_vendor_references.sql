-- First, identify products with invalid vendor_id references
CREATE TEMPORARY TABLE invalid_product_vendors AS
SELECT p.id, p.vendor_id
FROM products p
LEFT JOIN vendors v ON p.vendor_id = v.id
WHERE p.vendor_id IS NOT NULL AND v.id IS NULL;

-- Create a default vendor if none exists
INSERT INTO users (email, password, role, is_verified, account_status)
SELECT 'system@vendor.com', '$2a$10$default_password_hash', 'VENDOR', true, 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'system@vendor.com');

INSERT INTO vendors (user_id, store_name, contact_email, contact_phone, rating, review_count, total_orders, is_active)
SELECT u.id, 'System Vendor', 'system@vendor.com', '0000000000', 0.0, 0, 0, true
FROM users u 
WHERE u.email = 'system@vendor.com'
AND NOT EXISTS (SELECT 1 FROM vendors WHERE store_name = 'System Vendor');

-- Update products with invalid vendor_id to reference the default vendor
UPDATE products p
SET vendor_id = (SELECT id FROM vendors WHERE store_name = 'System Vendor')
WHERE EXISTS (
    SELECT 1 
    FROM invalid_product_vendors ipv 
    WHERE ipv.id = p.id
);

-- Drop temporary table
DROP TABLE invalid_product_vendors; 