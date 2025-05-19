package space.thinhtran.warehouse.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.thinhtran.warehouse.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResp {
    private Integer id;
    private String username;
    private String fullName;
    private String roleName;

    public static RegisterResp of(User user) {
        return RegisterResp.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roleName(user.getRole().getRoleName())
                .build();
    }
}
