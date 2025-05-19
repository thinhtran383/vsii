package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.dto.request.order.OrderDetailReq;
import space.thinhtran.warehouse.dto.response.order.OrderDetailResp;
import space.thinhtran.warehouse.entity.OrderDetail;
import space.thinhtran.warehouse.exception.NotFoundException;
import space.thinhtran.warehouse.repository.OrderDetailRepository;
import space.thinhtran.warehouse.repository.OrderRepository;
import space.thinhtran.warehouse.repository.ProductRepository;
import space.thinhtran.warehouse.service.IOrderDetailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDetailServiceImpl implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    /**
     * Adds a new order detail to an existing order.
     *
     * @param req The order detail data to add
     * @param userId The ID of the user adding the order detail
     * @return The created order detail information
     * @throws NotFoundException If the product or order is not found
     */
    public OrderDetailResp addOrderDetail(OrderDetailReq req, Integer userId) {
        validateProductExists(req.getProductId());
        validateOrderExists(req.getOrderId());

        orderDetailRepository.insertOrderDetail(
                req.getOrderId(),
                req.getProductId(),
                req.getQuantity(),
                req.getUnitPrice(),
                userId
        );

        OrderDetail orderDetail = orderDetailRepository.findByProductIdAndOrderId(req.getProductId(), req.getOrderId())
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.ORDER_DETAIL_NOT_FOUND, req.getProductId()));

        return modelMapper.map(orderDetail, OrderDetailResp.class);

    }

    /**
     * Deletes an order detail by product ID and order ID.
     *
     * @param productId The ID of the product in the order detail
     * @param orderId The ID of the order containing the detail
     */
    public void deleteOrderDetail(Integer productId, Integer orderId) {
        orderDetailRepository.deleteOrderDetail(
                orderId,
                productId
        );
    }

    private void validateProductExists(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(Constant.ErrorCode.PRODUCT_NOT_FOUND, productId);
        }
    }

    private void validateOrderExists(Integer orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(Constant.ErrorCode.ORDER_DETAIL_NOT_FOUND, orderId);
        }
    }
}
