package space.thinhtran.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.thinhtran.warehouse.dto.ApiResponse;
import space.thinhtran.warehouse.dto.PagedResponse;
import space.thinhtran.warehouse.dto.request.order.OrderReq;
import space.thinhtran.warehouse.dto.request.order.OrderSearchCriteria;
import space.thinhtran.warehouse.dto.response.order.OrderResp;
import space.thinhtran.warehouse.service.IOrderService;


@RestController
@RequestMapping("${api.v1}/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {
    private final IOrderService orderServiceImpl;

    @Operation(summary = "Get all orders", description = "Retrieves all orders with pagination and filtering")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PagedResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<OrderResp>>> getAllOrders(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
            @ParameterObject OrderSearchCriteria searchCriteria // annotation for OpenAPI
    ) {
        PagedResponse<OrderResp> orderRespPagedResponse = orderServiceImpl.getAllOrders(page, size, searchCriteria);
        ApiResponse<PagedResponse<OrderResp>> response = ApiResponse.<PagedResponse<OrderResp>>builder()
                .data(orderRespPagedResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new order", description = "Creates a new order with the provided information")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order created successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = OrderResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid order data supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResp>> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order data to create", 
                    required = true, content = @Content(schema = @Schema(implementation = OrderReq.class)))
            @RequestBody @Valid OrderReq orderReq
    ) {
        ApiResponse<OrderResp> response = ApiResponse.<OrderResp>builder()
                .data(orderServiceImpl.createOrder(orderReq))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Update an order", description = "Updates an existing order by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order updated successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = OrderResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid order data supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<OrderResp>> updateOrder(
            @Parameter(description = "ID of the order to update", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated order data", 
                    required = true, content = @Content(schema = @Schema(implementation = OrderReq.class)))
            @RequestBody @Valid OrderReq orderReq
    ) {
        ApiResponse<OrderResp> response = ApiResponse.<OrderResp>builder()
                .data(orderServiceImpl.updateOrder(id, orderReq))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Order deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(
            @Parameter(description = "ID of the order to delete", required = true) @PathVariable Integer id
    ) {
        orderServiceImpl.deleteOrder(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
