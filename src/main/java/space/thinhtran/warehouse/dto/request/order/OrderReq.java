package space.thinhtran.warehouse.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import space.thinhtran.warehouse.entity.enums.OrderType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderReq {
    private LocalDateTime orderDate = LocalDateTime.now();

    @NotNull(message = "{not.null}")
    private OrderType orderType;
    private String note;

    public static OrderReq of(LocalDateTime orderDate, OrderType orderType, String note) {
        return OrderReq.builder()
                .orderDate(orderDate)
                .orderType(orderType)
                .note(note)
                .build();
    }
}
