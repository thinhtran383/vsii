package space.thinhtran.warehouse.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginReq {
    @NotBlank(message = "{not.blank}")
    private String username;

    @NotBlank(message = "{not.blank}")
    private String password;
}
