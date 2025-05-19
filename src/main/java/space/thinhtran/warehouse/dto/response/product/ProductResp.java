package space.thinhtran.warehouse.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.thinhtran.warehouse.entity.Product;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResp {
    private Integer id;
    private String productName;
    private String category;
    private String unit;
    private BigDecimal price;
    private Integer quantity;


    public static ProductResp of(Product product) {
        return ProductResp.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .category(product.getCategory())
                .unit(product.getUnit())
                .price(product.getPrice())
                .quantity(0)
                .build();
    }
}
