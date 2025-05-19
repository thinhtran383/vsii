package space.thinhtran.warehouse.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditEntity {

    @CreationTimestamp
    private LocalDateTime created_at;

    @CreatedBy
    private Integer created_by;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    private Boolean is_delete = false;

}