package space.thinhtran.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.thinhtran.warehouse.common.Constant;
import space.thinhtran.warehouse.dto.PagedResponse;
import space.thinhtran.warehouse.dto.request.order.OrderReq;
import space.thinhtran.warehouse.dto.request.order.OrderSearchCriteria;
import space.thinhtran.warehouse.dto.response.order.OrderResp;
import space.thinhtran.warehouse.entity.Order;
import space.thinhtran.warehouse.exception.NotFoundException;
import space.thinhtran.warehouse.repository.OrderRepository;
import space.thinhtran.warehouse.repository.spec.OrderSpecification;
import space.thinhtran.warehouse.service.IOrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves all orders with pagination and filtering based on search criteria.
     *
     * @param page The page number (zero-based)
     * @param size The size of the page
     * @param searchCriteria The criteria used to filter orders
     * @return A paged response containing order information
     */
    @Transactional(readOnly = true)
    public PagedResponse<OrderResp> getAllOrders(int page, int size, OrderSearchCriteria searchCriteria) {
        Specification<Order> spec = Specification.where(OrderSpecification.hasOrderId(searchCriteria.getOrderId()))
                .and(OrderSpecification.hasType(searchCriteria.getOrderType()))
                .and(OrderSpecification.hasOrderDateBetween(searchCriteria.getStartDate(), searchCriteria.getEndDate()));

        Page<Order> orderPage = orderRepository.findAll(spec, PageRequest.of(page, size));

        List<OrderResp> orderRespList = orderPage.getContent().stream()
                .map(OrderResp::of)
                .toList();

        return PagedResponse.<OrderResp>builder()
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .elements(orderRespList)
                .build();
    }

    /**
     * Creates a new order.
     *
     * @param orderReq The order data to create
     * @return The created order's information
     */
    @Transactional
    public OrderResp createOrder(OrderReq orderReq) {
        Order order = modelMapper.map(orderReq, Order.class);
        return OrderResp.of(orderRepository.save(order));
    }

    /**
     * Deletes an order by its ID.
     *
     * @param orderId The ID of the order to delete
     * @throws NotFoundException If the order with the given ID is not found
     */
    @Transactional
    public void deleteOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.ORDER_NOT_FOUND, orderId));
        orderRepository.delete(order);
    }

    /**
     * Updates an existing order by its ID.
     *
     * @param orderId The ID of the order to update
     * @param orderReq The updated order data
     * @return The updated order's information
     * @throws NotFoundException If the order with the given ID is not found
     */
    @Transactional
    public OrderResp updateOrder(int orderId, OrderReq orderReq) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Constant.ErrorCode.ORDER_NOT_FOUND, orderId));
        modelMapper.map(orderReq, order);

        // Update the order with the new values
        order.setOrderDate(orderReq.getOrderDate());
        order.setType(orderReq.getOrderType());
        order.setNote(orderReq.getNote());

        return OrderResp.of(orderRepository.save(order));
    }
}
