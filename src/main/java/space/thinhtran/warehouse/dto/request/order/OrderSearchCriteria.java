package space.thinhtran.warehouse.dto.request.order;

import lombok.*;
import space.thinhtran.warehouse.entity.enums.OrderType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSearchCriteria {
    private Integer orderId;
    private OrderType orderType;
    private LocalDate startDate;
    private LocalDate endDate;
}
