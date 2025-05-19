package space.thinhtran.warehouse.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PagedResponse<T> {
    private List<T> elements;
    private int totalPages;
    private long totalElements;
}
