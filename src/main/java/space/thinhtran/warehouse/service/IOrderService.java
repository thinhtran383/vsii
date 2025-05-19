package space.thinhtran.warehouse.service;

import space.thinhtran.warehouse.dto.PagedResponse;
import space.thinhtran.warehouse.dto.request.order.OrderReq;
import space.thinhtran.warehouse.dto.request.order.OrderSearchCriteria;
import space.thinhtran.warehouse.dto.response.order.OrderResp;

public interface IOrderService {
    PagedResponse<OrderResp> getAllOrders(int page, int size, OrderSearchCriteria searchCriteria);
    OrderResp createOrder(OrderReq orderReq);
    void deleteOrder(int orderId);
    OrderResp updateOrder(int orderId, OrderReq orderReq);
}
