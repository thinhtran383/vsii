package space.thinhtran.warehouse.service;

import space.thinhtran.warehouse.dto.request.order.OrderDetailReq;
import space.thinhtran.warehouse.dto.response.order.OrderDetailResp;

public interface IOrderDetailService {
    OrderDetailResp addOrderDetail(OrderDetailReq req, Integer userId);
    void deleteOrderDetail(Integer productId, Integer orderId);
}
