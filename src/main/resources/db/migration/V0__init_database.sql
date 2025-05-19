CREATE TABLE Products
(
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category     VARCHAR(100),
    unit         VARCHAR(50),
    price        DECIMAL(10, 2),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT NULL,
    created_by int
);

CREATE TABLE Stock
(
    stock_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    quantity   INT      DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    created_by int,
    FOREIGN KEY (product_id) REFERENCES Products (product_id)
);

CREATE TABLE Orders
(
    order_id   INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    type       ENUM('IMPORT', 'EXPORT') NOT NULL,
    note       TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by int,
    updated_at DATETIME DEFAULT NULL
);

CREATE TABLE OrderDetails
(
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id        INT,
    product_id      INT,
    quantity        INT,
    unit_price      DECIMAL(10, 2),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by      int,
    updated_at      DATETIME DEFAULT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders (order_id),
    FOREIGN KEY (product_id) REFERENCES Products (product_id)
);

