package space.thinhtran.warehouse.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResp {
    private String username;
    private String fullName;
    private String accessToken;
    private String role;
}
