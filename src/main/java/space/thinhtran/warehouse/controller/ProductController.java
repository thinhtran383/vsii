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
import space.thinhtran.warehouse.dto.request.product.ProductReq;
import space.thinhtran.warehouse.dto.request.product.ProductSearchCriteria;
import space.thinhtran.warehouse.dto.response.product.ProductResp;
import space.thinhtran.warehouse.service.IProductService;

@RestController
@RequestMapping("${api.v1}/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    private final IProductService productService;

    @Operation(summary = "Get all products", description = "Retrieves all products with pagination and filtering")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PagedResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResp>>> getAllProduct(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
            @ParameterObject ProductSearchCriteria searchCriteria
    ) {
        PagedResponse<ProductResp> productRespPagedResponse = productService.getAllProduct(page, size, searchCriteria);
        ApiResponse<PagedResponse<ProductResp>> response = ApiResponse.<PagedResponse<ProductResp>>builder()
                .data(productRespPagedResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided information")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ProductResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResp>> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product data to create", 
                    required = true, content = @Content(schema = @Schema(implementation = ProductReq.class)))
            @RequestBody @Valid ProductReq productReq
    ) {
        ApiResponse<ProductResp> response = ApiResponse.<ProductResp>builder()
                .data(productService.createProduct(productReq))
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Update a product", description = "Updates an existing product by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ProductResp.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data supplied",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResp>> updateProduct(
            @Parameter(description = "ID of the product to update", required = true) @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product data", 
                    required = true, content = @Content(schema = @Schema(implementation = ProductReq.class)))
            @RequestBody @Valid ProductReq productReq
    ) {

        ApiResponse<ProductResp> response = ApiResponse.<ProductResp>builder()
                .data(productService.updateProduct(id, productReq))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found",
                content = @Content),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true) @PathVariable Integer id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}