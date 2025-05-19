package space.thinhtran.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import space.thinhtran.warehouse.dto.ApiResponse;
import space.thinhtran.warehouse.dto.request.order.OrderDetailReq;
import space.thinhtran.warehouse.dto.response.order.OrderDetailResp;
import space.thinhtran.warehouse.entity.User;
import space.thinhtran.warehouse.service.impl.OrderDetailServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1}/order-details")
@Tag(name = "Order Detail Management", description = "APIs for managing order details")
public class OrderDetailController {
    private final OrderDetailServiceImpl orderDetailService;

    @Operation(summary = "Add a product to an order", description = "Adds a new product to an existing order")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product added to order successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = OrderDetailResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid details supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product or order not found",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @PostMapping()
    public ResponseEntity<ApiResponse<OrderDetailResp>> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order detail to add", 
                    required = true, content = @Content(schema = @Schema(implementation = OrderDetailReq.class)))
            @RequestBody OrderDetailReq orderDetailReq,
            @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {

        ApiResponse<OrderDetailResp> response = ApiResponse.<OrderDetailResp>builder()
                .data(orderDetailService.addOrderDetail(orderDetailReq, user.getId()))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete a product from an order", description = "Removes a product from an existing order")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Product removed from order successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product or order not found",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @DeleteMapping("{productId}/{orderId}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID of the product to remove", required = true) @PathVariable Integer productId,
            @Parameter(description = "ID of the order containing the product", required = true) @PathVariable Integer orderId
    ) {
        orderDetailService.deleteOrderDetail(productId, orderId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
