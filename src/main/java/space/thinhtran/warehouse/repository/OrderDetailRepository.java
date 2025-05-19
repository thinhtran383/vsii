package space.thinhtran.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import space.thinhtran.warehouse.dto.request.order.OrderDetailReq;
import space.thinhtran.warehouse.entity.Order;
import space.thinhtran.warehouse.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Procedure(procedureName = "insert_order_detail")
    void insertOrderDetail(
            @Param("p_order_id") Integer orderId,
            @Param("p_product_id") Integer productId,
            @Param("p_quantity") Integer quantity,
            @Param("p_unit_price") BigDecimal unitPrice,
            @Param("p_user_id") Integer userId
    );

    @Procedure(procedureName = "delete_order_detail")
    void deleteOrderDetail(
            @Param("p_order_id") Integer orderId,
            @Param("p_product_id") Integer productId
    );

    Integer order(Order order);

    Optional<OrderDetail> findByProductIdOrOrderId(Integer productId, Integer orderId);

    Optional<OrderDetail> findByProductIdAndOrderId(Integer product_id, Integer order_id);
}
