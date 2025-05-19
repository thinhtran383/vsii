package space.thinhtran.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.thinhtran.warehouse.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
