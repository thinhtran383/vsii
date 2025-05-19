package space.thinhtran.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import space.thinhtran.warehouse.util.HttpMethodConverter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Permissions", schema = "InventoryDB")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false)
    private Integer id;

//    @Size(max = 10)
//    @NotNull
//    @Column(name = "http_method", nullable = false, length = 10)
//    private String httpMethod;


    @Convert(converter = HttpMethodConverter.class)
    @Column(name = "http_method", nullable = false)
    private Set<String> httpMethods;

    @Size(max = 255)
    @NotNull
    @Column(name = "api_endpoint", nullable = false)
    private String apiEndpoint;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

}