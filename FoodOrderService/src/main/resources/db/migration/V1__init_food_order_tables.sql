-- 1. Create the Menu Table (Used by Order)
CREATE TABLE menu (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255),
                      price NUMERIC(10, 2),
                      category VARCHAR(255),
                      images VARCHAR(255)
);

-- 2. Create the MenuItem Table (Used by FoodOrder)
CREATE TABLE menu_item (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255),
                           price NUMERIC(10, 2)
);

-- 3. Create the FoodOrder Table
CREATE TABLE food_order (
                            id BIGSERIAL PRIMARY KEY,
                            booking_id BIGINT,
                            total_price NUMERIC(10, 2),
                            status VARCHAR(50) DEFAULT 'PENDING'
);

-- 4. Create the Many-to-Many Join Table between FoodOrder and MenuItem
CREATE TABLE food_order_item (
                                 order_id BIGINT NOT NULL,
                                 menu_item_id BIGINT NOT NULL,
                                 PRIMARY KEY (order_id, menu_item_id),
                                 CONSTRAINT fk_food_order FOREIGN KEY (order_id) REFERENCES food_order (id) ON DELETE CASCADE,
                                 CONSTRAINT fk_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_item (id) ON DELETE CASCADE
);

-- 5. Create the Orders Table
CREATE TABLE orders (
                        order_id BIGSERIAL PRIMARY KEY,
                        order_number VARCHAR(255),
                        order_date DATE,
                        menu_id BIGINT,
                        quantity INT,
                        total_price NUMERIC(10, 2),
                        order_status VARCHAR(255),
                        table_num VARCHAR(50),
                        CONSTRAINT fk_orders_menu FOREIGN KEY (menu_id) REFERENCES menu (id)
);
