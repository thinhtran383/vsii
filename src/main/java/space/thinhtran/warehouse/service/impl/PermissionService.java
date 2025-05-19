package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.entity.Permission;
import space.thinhtran.warehouse.entity.Role;
import space.thinhtran.warehouse.exception.NotFoundException;
import space.thinhtran.warehouse.repository.RoleRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final RoleRepository roleRepository;
//    public Boolean hasPermission(Set<Role> roles, String method, String uri) {
//        AntPathMatcher matcher = new AntPathMatcher();
//
//        for (Role role : roles) {
//            Set<Permission> permissions = RolePermissionMapping.getPermissions(role);
//
//            for (Permission permission : permissions) {
//                if (permission.getMethod().equalsIgnoreCase(method)
//                        && matcher.match(permission.getPath(), uri)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

    @Transactional(readOnly = true)
    public Boolean hasPermission(String roleName, String httpMethod, String requestUri) {
        Role role = roleRepository.findWithPermissionsByRoleName(roleName)
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.AUTH_PERMISSION_ROLE_NOT_FOUND, roleName));

        Set<Permission> permissions = role.getPermissions();

        for (Permission p : permissions) {
            if (p.getHttpMethod().equalsIgnoreCase(httpMethod)) {
                if (matchEndpoint(p.getApiEndpoint(), requestUri)) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean matchEndpoint(String pattern, String uri) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, uri);
    }
}
