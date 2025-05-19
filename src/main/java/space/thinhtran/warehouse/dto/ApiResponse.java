package space.thinhtran.warehouse.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ApiResponse<T> {
    private T data;
    private String message;
}