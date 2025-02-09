-- Update null values in products table with default values
UPDATE products 
SET image_url = 'default-product-image.jpg' 
WHERE image_url IS NULL;

UPDATE products 
SET is_active = true 
WHERE is_active IS NULL;

UPDATE products 
SET is_featured = false 
WHERE is_featured IS NULL;

UPDATE products 
SET order_count = 0 
WHERE order_count IS NULL;

UPDATE products 
SET rating = 0.0 
WHERE rating IS NULL;

UPDATE products 
SET review_count = 0 
WHERE review_count IS NULL;

UPDATE products 
SET stock = 0 
WHERE stock IS NULL;

-- Add not null constraints after updating values
ALTER TABLE products 
    ALTER COLUMN image_url SET NOT NULL,
    ALTER COLUMN is_active SET NOT NULL,
    ALTER COLUMN is_featured SET NOT NULL,
    ALTER COLUMN order_count SET NOT NULL,
    ALTER COLUMN rating SET NOT NULL,
    ALTER COLUMN review_count SET NOT NULL,
    ALTER COLUMN stock SET NOT NULL; 