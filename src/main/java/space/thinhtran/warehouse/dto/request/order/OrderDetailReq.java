package space.thinhtran.warehouse.dto.request.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailReq {

    @NotNull(message = "{not.null}")
    private Integer orderId;

    @NotNull(message = "{not.null}")
    private Integer productId;

    @NotNull(message = "{not.null}")
    @Min(value = 1, message = "{greater.than}")
    private Integer quantity;

    @NotNull(message = "{not.null}")
    @DecimalMin(value = "0.00", message = "{greater.than}")
    private BigDecimal unitPrice;

}
