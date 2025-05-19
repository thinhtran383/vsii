package space.thinhtran.warehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import space.thinhtran.warehouse.dto.response.product.ProductResp;
import space.thinhtran.warehouse.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
    SELECT new space.thinhtran.warehouse.dto.response.product.ProductResp(
        p.id,
        p.productName,
        p.category,
        p.unit,
        p.price,
        s.quantity
    )
    FROM Product p
    JOIN Stock s ON s.product.id = p.id
    """)
    Page<ProductResp> fetchAllProductWithStock(Pageable pageable);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
