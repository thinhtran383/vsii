package space.thinhtran.warehouse.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.entity.User;
import space.thinhtran.warehouse.exception.MissingTokenException;
import space.thinhtran.warehouse.exception.NoPermissionException;
import space.thinhtran.warehouse.service.IPermissionService;
import space.thinhtran.warehouse.service.impl.PermissionServiceImpl;
import space.thinhtran.warehouse.util.ApiEndpointSecurityInspector;
import space.thinhtran.warehouse.util.JwtUtil;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ApiEndpointSecurityInspector apiEndpointSecurityInspector;
    private final IPermissionService permissionService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (!apiEndpointSecurityInspector.isUnsecureRequest(request)) {
            String token = extractTokenFromHeader(request);
            validateTokenAndPermissions(token, request);
            authenticateUser(token);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new MissingTokenException(Constant.ErrorCode.AUTH_JWT_MISSING);
        }

        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    private void validateTokenAndPermissions(String token, HttpServletRequest request) {
        jwtUtil.validateToken(token);

        String role = jwtUtil.getRole(token);

        String method = request.getMethod();
        String uri = request.getRequestURI();

        boolean allowed = permissionService.hasPermission(role, method, uri);
        if (!allowed) {
            throw new NoPermissionException(Constant.ErrorCode.AUTH_NO_PERMISSION);
        }
    }

    private void authenticateUser(String token) {
        String username = jwtUtil.extractAccessToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            authenticationToken.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
