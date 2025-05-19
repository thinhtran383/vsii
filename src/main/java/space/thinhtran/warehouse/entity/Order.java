package space.thinhtran.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import space.thinhtran.warehouse.entity.enums.OrderType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Orders", schema = "InventoryDB")
@SQLDelete(sql = "Update Orders set is_delete = true where order_id = ?")
@SQLRestriction("is_delete = false")
public class Order extends AbstractAuditEntity {
    @Id
    @Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Column(name = "note")
    private String note;

}