package space.thinhtran.warehouse.service;

import space.thinhtran.warehouse.dto.PagedResponse;
import space.thinhtran.warehouse.dto.request.product.ProductReq;
import space.thinhtran.warehouse.dto.request.product.ProductSearchCriteria;
import space.thinhtran.warehouse.dto.response.product.ProductResp;

public interface IProductService {
    PagedResponse<ProductResp> getAllProduct(int page, int size, ProductSearchCriteria searchCriteria);

    ProductResp createProduct(ProductReq product);

    ProductResp updateProduct(Integer id, ProductReq product);

    void deleteProduct(Integer id);
}
