package space.thinhtran.warehouse.dto.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import space.thinhtran.warehouse.entity.Order;
import space.thinhtran.warehouse.entity.enums.OrderType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderResp {
    private Integer orderId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderDate;

    private OrderType orderType;

    private String note;

    private String fullName;

    public static OrderResp of(Order order) {
        return OrderResp.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderType(order.getType())
                .note(order.getNote())
                .build();
    }
}
