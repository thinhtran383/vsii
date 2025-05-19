package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.dto.PagedResponse;
import space.thinhtran.warehouse.dto.request.product.ProductReq;
import space.thinhtran.warehouse.dto.request.product.ProductSearchCriteria;
import space.thinhtran.warehouse.dto.response.product.ProductResp;
import space.thinhtran.warehouse.entity.Product;
import space.thinhtran.warehouse.entity.Stock;
import space.thinhtran.warehouse.exception.NotFoundException;
import space.thinhtran.warehouse.repository.ProductRepository;
import space.thinhtran.warehouse.repository.StockRepository;
import space.thinhtran.warehouse.service.IProductService;
import space.thinhtran.warehouse.repository.spec.ProductSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ProductService")
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    private final ModelMapper modelMapper;

    /**
     * Retrieves all products with pagination and filtering based on search criteria.
     *
     * @param page The page number (zero-based)
     * @param size The size of the page
     * @param searchCriteria The criteria used to filter products
     * @return A paged response containing product information
     */
    public PagedResponse<ProductResp> getAllProduct(int page, int size, ProductSearchCriteria searchCriteria) {

        Specification<Product> spec = Specification.where(ProductSpecification.hasName(searchCriteria.getName()))
                .and(ProductSpecification.hasProductId(searchCriteria.getProductId()))
                .and(ProductSpecification.hasCategory(searchCriteria.getCategory()));

        Page<Product> productPage = productRepository.findAll(spec, PageRequest.of(page, size));

        List<ProductResp> productRespList = productPage.getContent().stream()
                .map(ProductResp::of)
                .collect(Collectors.toList());

        return PagedResponse.<ProductResp>builder()
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .elements(productRespList)
                .build();
    }
    
    /**
     * Creates a new product and initializes its stock.
     *
     * @param product The product data to create
     * @return The created product's information
     */
    @Override
    @Transactional
    public ProductResp createProduct(ProductReq product) {
        Product productEntity = modelMapper.map(product, Product.class);
        Product savedProduct = productRepository.save(productEntity);

        Stock stock = new Stock();
        stock.setProduct(savedProduct);
        stock.setQuantity(0);

        stockRepository.save(stock);

        return ProductResp.of(savedProduct);
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param id The ID of the product to update
     * @param product The updated product data
     * @return The updated product's information
     * @throws NotFoundException If the product with the given ID is not found
     */
    @Override
    @Transactional
    public ProductResp updateProduct(Integer id, ProductReq product) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.PRODUCT_NOT_FOUND, id));

        productEntity.setProductName(product.getProductName());
        productEntity.setPrice(product.getPrice());
        productEntity.setCategory(product.getCategory());

        Product savedProduct = productRepository.save(productEntity);
        return ProductResp.of(savedProduct);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @throws NotFoundException If the product with the given ID is not found
     */
    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.PRODUCT_NOT_FOUND, id));

        productRepository.delete(product);
    }


}
