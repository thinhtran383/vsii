package space.thinhtran.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.thinhtran.warehouse.annotation.PublicEndpoint;
import space.thinhtran.warehouse.dto.ApiResponse;
import space.thinhtran.warehouse.dto.request.auth.LoginReq;
import space.thinhtran.warehouse.dto.request.auth.RegisterReq;
import space.thinhtran.warehouse.dto.response.auth.LoginResp;
import space.thinhtran.warehouse.dto.response.auth.RegisterResp;
import space.thinhtran.warehouse.service.impl.AuthServiceImpl;

@RestController
@RequestMapping("${api.v1}/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {
    private final AuthServiceImpl authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with STAFF role")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = RegisterResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user data supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username already exists",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResp>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data", 
                    required = true, content = @Content(schema = @Schema(implementation = RegisterReq.class)))
            @RequestBody @Valid RegisterReq req
    ) {
        RegisterResp registerResp = authService.createUser(req);
        ApiResponse<RegisterResp> response = ApiResponse.<RegisterResp>builder()
                .data(registerResp)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns an access token")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = LoginResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid credentials",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication failed",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @PostMapping("/login")
    @PublicEndpoint
    public ResponseEntity<ApiResponse<LoginResp>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login credentials", 
                    required = true, content = @Content(schema = @Schema(implementation = LoginReq.class)))
            @RequestBody @Valid LoginReq req
    ) {
        LoginResp loginResp = authService.login(req);
        ApiResponse<LoginResp> response = ApiResponse.<LoginResp>builder()
                .data(loginResp)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
