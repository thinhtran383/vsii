package space.thinhtran.warehouse.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchCriteria {
    private Integer productId;
    private String name;
    private String category;
}
