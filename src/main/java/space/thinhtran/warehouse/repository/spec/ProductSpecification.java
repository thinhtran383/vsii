package space.thinhtran.warehouse.repository.spec;

import org.springframework.data.jpa.domain.Specification;
import space.thinhtran.warehouse.entity.Product;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) return null;
            return criteriaBuilder.like(root.get("productName"), "%" + name + "%");
        };
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isEmpty()) return null;
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Product> hasProductId(Integer productId) {
        return ((root, query, criteriaBuilder) -> {
            if (productId == null || productId < 1) return null;
            return criteriaBuilder.equal(root.get("productId"), productId);
        });
    }

}
