package space.thinhtran.warehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import space.thinhtran.warehouse.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAll(Specification<Order> spec, Pageable pageable);
}
