package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.entity.Permission;
import space.thinhtran.warehouse.entity.Role;
import space.thinhtran.warehouse.exception.NoPermissionException;
import space.thinhtran.warehouse.repository.PermissionRepository;
import space.thinhtran.warehouse.repository.RoleRepository;
import space.thinhtran.warehouse.service.IPermissionService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements IPermissionService {
    private static final String UPDATED = "Auto updated";
    private static final String SYNCED = "Auto synced";

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ApplicationContext applicationContext;


    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void syncPermissions() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        int syncedCount = 0;

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();

            Set<String> paths = new HashSet<>();
            if (info.getPathPatternsCondition() != null) {
                info.getPathPatternsCondition().getPatterns()
                        .forEach(p -> paths.add(p.getPatternString()));
            } else if (info.getPatternsCondition() != null) {
                paths.addAll(info.getPatternsCondition().getPatterns());
            }

            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();

            if (methods.isEmpty()) continue;

            Set<String> httpMethods = methods.stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            for (String path : paths) {
                Optional<Permission> existing = permissionRepository.findByApiEndpoint(path);

                Permission permission = createOrUpdatePermission(path, existing, httpMethods);
                permissionRepository.save(permission);

                syncedCount++;
            }
        }

        log.info("Total permissions synced: {} ", syncedCount);
    }


    @Transactional(readOnly = true)
    public Boolean hasPermission(String roleName, String httpMethod, String requestUri) {
        Role role = roleRepository.findWithPermissionsByRoleName(roleName)
                .orElseThrow(() -> new NoPermissionException(Constant.ErrorCode.AUTH_NO_PERMISSION));

        String method = httpMethod.toUpperCase();

        return role.getPermissions().stream()
                .anyMatch(permission ->
                        permission.getHttpMethods().contains(method)
                                && matchEndpoint(permission.getApiEndpoint(), requestUri)
                );
    }

    private boolean matchEndpoint(String pattern, String uri) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, uri);
    }

    private static Permission createOrUpdatePermission(String path, Optional<Permission> existing, Set<String> httpMethods) {
        Permission permission;
        if (existing.isPresent()) {
            permission = existing.get();
            permission.setHttpMethods(httpMethods);
            permission.setDescription(UPDATED);
        } else {
            permission = new Permission();
            permission.setApiEndpoint(path);
            permission.setHttpMethods(httpMethods);
            permission.setDescription(SYNCED);
        }
        return permission;
    }

}
