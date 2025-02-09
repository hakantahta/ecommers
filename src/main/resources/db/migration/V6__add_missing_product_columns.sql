-- Drop the existing products table if it exists
DROP TABLE IF EXISTS products CASCADE;

-- Create the products table with all required columns
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    discounted_price DECIMAL(10,2),
    image_url VARCHAR(255) DEFAULT 'default-product-image.jpg' NOT NULL,
    stock INTEGER DEFAULT 0 NOT NULL,
    is_active BOOLEAN DEFAULT true NOT NULL,
    is_featured BOOLEAN DEFAULT false NOT NULL,
    order_count INTEGER DEFAULT 0 NOT NULL,
    rating DOUBLE PRECISION DEFAULT 0.0 NOT NULL,
    review_count INTEGER DEFAULT 0 NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    category_id BIGINT,
    vendor_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Add foreign key constraints
ALTER TABLE products 
    ADD CONSTRAINT fk_products_category 
    FOREIGN KEY (category_id) 
    REFERENCES categories(id);

ALTER TABLE products 
    ADD CONSTRAINT fk_products_vendor 
    FOREIGN KEY (vendor_id) 
    REFERENCES vendors(id); 