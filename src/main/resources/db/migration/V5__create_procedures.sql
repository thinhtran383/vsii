DELIMITER $$

CREATE PROCEDURE insert_order_detail (
    IN p_order_id INT,
    IN p_product_id INT,
    IN p_quantity INT,
    IN p_unit_price DECIMAL(10, 2),
    IN p_user_id INT
)
BEGIN
    DECLARE order_type ENUM('IMPORT', 'EXPORT');
    DECLARE existing_quantity INT;

SELECT type INTO order_type
FROM Orders
WHERE order_id = p_order_id;

SELECT quantity INTO existing_quantity
FROM OrderDetails
WHERE order_id = p_order_id AND product_id = p_product_id;

IF existing_quantity IS NOT NULL THEN
UPDATE OrderDetails
SET quantity = quantity + p_quantity,
    unit_price = p_unit_price,
    updated_at = NOW(),
    created_by = p_user_id
WHERE order_id = p_order_id AND product_id = p_product_id;
ELSE
        INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price, created_by)
        VALUES (p_order_id, p_product_id, p_quantity, p_unit_price, p_user_id);
END IF;

    IF order_type = 'IMPORT' THEN
UPDATE Stock
SET quantity = quantity + p_quantity,
    updated_at = NOW(),
    created_by = p_user_id
WHERE product_id = p_product_id;
ELSE
UPDATE Stock
SET quantity = quantity - p_quantity,
    updated_at = NOW(),
    created_by = p_user_id
WHERE product_id = p_product_id;
END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE delete_order_detail (
    IN p_order_id INT,
    IN p_product_id INT
)
BEGIN
    DECLARE order_type ENUM('IMPORT', 'EXPORT');
    DECLARE p_quantity INT;

    -- Lấy loại đơn hàng
SELECT type INTO order_type
FROM Orders
WHERE order_id = p_order_id;

-- Lấy số lượng chi tiết đã đặt
SELECT quantity INTO p_quantity
FROM OrderDetails
WHERE order_id = p_order_id AND product_id = p_product_id;

-- Xoá chi tiết
DELETE FROM OrderDetails
WHERE order_id = p_order_id AND product_id = p_product_id;

-- Cập nhật tồn kho
IF order_type = 'IMPORT' THEN
UPDATE Stock
SET quantity = quantity - p_quantity,
    updated_at = NOW()
WHERE product_id = p_product_id;
ELSE
UPDATE Stock
SET quantity = quantity + p_quantity,
    updated_at = NOW()
WHERE product_id = p_product_id;
END IF;
END$$

DELIMITER ;
