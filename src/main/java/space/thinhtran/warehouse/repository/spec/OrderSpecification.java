package space.thinhtran.warehouse.repository.spec;

import org.springframework.data.jpa.domain.Specification;
import space.thinhtran.warehouse.entity.Order;
import space.thinhtran.warehouse.entity.enums.OrderType;

import java.time.LocalDate;

public class OrderSpecification {
    public static Specification<Order> hasOrderId(Integer orderId) {
        return (root, query, criteriaBuilder) -> {
            if (orderId == null) return null;
            return criteriaBuilder.equal(root.get("id"), orderId);
        };
    }

    public static Specification<Order> hasType(OrderType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) return null;
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<Order> hasOrderDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) return null;
            return criteriaBuilder.between(root.get("orderDate"), startDate, endDate);
        };
    }
}
