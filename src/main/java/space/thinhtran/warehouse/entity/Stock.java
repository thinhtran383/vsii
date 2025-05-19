package space.thinhtran.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Stock", schema = "InventoryDB")
@SQLDelete(sql = "Update Stock set is_delete = true where product_id = ?")
@SQLRestriction("is_delete = false")
public class Stock extends AbstractAuditEntity {
    @Id
    @Column(name = "stock_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ColumnDefault("0")
    @Column(name = "quantity")
    private Integer quantity;
}