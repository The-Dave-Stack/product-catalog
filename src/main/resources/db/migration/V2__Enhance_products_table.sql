-- Add new columns to the products table
ALTER TABLE products ADD COLUMN stock_quantity INTEGER NOT NULL DEFAULT 0;
ALTER TABLE products ADD COLUMN min_stock_level INTEGER DEFAULT 0;
ALTER TABLE products ADD COLUMN image_url VARCHAR(500);
ALTER TABLE products ADD COLUMN weight NUMERIC(10, 3);
ALTER TABLE products ADD COLUMN dimensions VARCHAR(255);
ALTER TABLE products ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE products ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE products ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

-- Update category column to use ENUM values (existing varchar will be compatible)
-- Add check constraint for category values
ALTER TABLE products ADD CONSTRAINT check_category 
CHECK (category IN ('Electronics', 'Clothing', 'Books', 'Home & Garden', 'Sports & Outdoors', 
                    'Toys & Games', 'Health & Beauty', 'Food & Beverages', 'Automotive', 'Other'));

-- Add indexes for performance
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_active ON products(active);
CREATE INDEX idx_products_deleted ON products(deleted);
CREATE INDEX idx_products_stock_quantity ON products(stock_quantity);