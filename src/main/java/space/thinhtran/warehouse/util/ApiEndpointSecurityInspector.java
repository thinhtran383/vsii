package space.thinhtran.warehouse.util;

import io.swagger.v3.oas.models.PathItem;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import space.thinhtran.warehouse.annotation.PublicEndpoint;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ApiEndpointSecurityInspector {
    private final RequestMappingHandlerMapping handlerMapping;

    private static final Set<String> SWAGGER_V3_PATHS = Set.of(
            "/v3/api-docs/",
            "/v3/api-docs/**",
            "/api-docs",
            "/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );

    private static final Set<String> DEFAULT_ENDPOINT_SPRING_HANDLERS = Set.of(
            "/Error",
            "/error",
            "/sw.js"
    );

    @Getter
    private final Set<String> publicGetEndpoints = new HashSet<>();
    @Getter
    private final Set<String> publicPostEndpoints = new HashSet<>();
    @Getter
    private final Set<String> publicPutEndpoints = new HashSet<>();
    @Getter
    private final Set<String> publicDeleteEndpoints = new HashSet<>();

    @PostConstruct
    public void init() {
        final var handlerMethods = handlerMapping.getHandlerMethods();
        Map<HttpMethod, Set<String>> methodToEndpointsMap = new HashMap<>();

        methodToEndpointsMap.put(HttpMethod.GET, publicGetEndpoints);
        methodToEndpointsMap.put(HttpMethod.POST, publicPostEndpoints);
        methodToEndpointsMap.put(HttpMethod.PUT, publicPutEndpoints);
        methodToEndpointsMap.put(HttpMethod.DELETE, publicDeleteEndpoints);

        handlerMethods.forEach((requestInfo, handlerMethod) -> {
            if (handlerMethod.hasMethodAnnotation(PublicEndpoint.class)) {
                var apiPaths = requestInfo.getPathPatternsCondition().getPatternValues();

                requestInfo.getMethodsCondition().getMethods().forEach(requestMethod -> {
                    HttpMethod httpMethod = requestMethod.asHttpMethod();
                    if (methodToEndpointsMap.containsKey(httpMethod)) {
                        methodToEndpointsMap.get(httpMethod).addAll(apiPaths);
                    }
                });
            }
        });

        publicGetEndpoints.addAll(SWAGGER_V3_PATHS);
        publicPostEndpoints.addAll(DEFAULT_ENDPOINT_SPRING_HANDLERS);
    }

    public boolean isUnsecureRequest(final HttpServletRequest request) {
        final PathItem.HttpMethod requestMethod = PathItem.HttpMethod.valueOf(request.getMethod());
        Set<String> unSecureApiPaths = getUnsecuredApiPaths(requestMethod);

        return unSecureApiPaths.stream()
                .anyMatch(path -> new AntPathMatcher().match(path, request.getServletPath()));

    }

    private Set<String> getUnsecuredApiPaths(@NonNull final PathItem.HttpMethod httpMethod) {
        return switch (httpMethod) {
            case GET -> publicGetEndpoints;
            case POST -> publicPostEndpoints;
            case PUT -> publicPutEndpoints;
            default -> Collections.emptySet();
        };
    }
}
