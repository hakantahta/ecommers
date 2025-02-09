-- Eğer kolonlar varsa önce silelim
ALTER TABLE products DROP COLUMN IF EXISTS isactive;
ALTER TABLE products DROP COLUMN IF EXISTS isfeatured;

-- Doğru isimlerle kolonları ekleyelim
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS is_featured BOOLEAN NOT NULL DEFAULT false; 