package space.thinhtran.warehouse.dto.request.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReq {

    @NotBlank(message = "{not.blank}")
    private String productName;

    @NotBlank(message = "{not.blank}")
    private String category;

    @NotBlank(message = "{not.blank}")
    private String unit;

    @NotNull(message = "{not.null}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{greater.than}")
    private BigDecimal price;

    @NotNull(message = "{not.null}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{greater.than}")
    private BigDecimal salePrice;

    public static ProductReq of(String productName, String category, String unit, BigDecimal price, BigDecimal salePrice) {
        return ProductReq.builder()
                .productName(productName)
                .category(category)
                .unit(unit)
                .price(price)
                .salePrice(salePrice)
                .build();
    }
}