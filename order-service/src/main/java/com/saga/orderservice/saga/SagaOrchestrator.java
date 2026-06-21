package com.saga.orderservice.saga;

import com.saga.orderservice.client.InventoryClient;
import com.saga.orderservice.client.StockItemRequest;
import com.saga.orderservice.domain.OrderStatus;
import com.saga.orderservice.dto.CreateOrderRequest;
import com.saga.orderservice.dto.OrderItemResponse;
import com.saga.orderservice.dto.OrderResponse;
import com.saga.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class SagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(SagaOrchestrator.class);

    private final OrderService orderService;
    private final InventoryClient inventoryClient;

    public SagaOrchestrator(OrderService orderService, InventoryClient inventoryClient) {
        this.orderService = orderService;
        this.inventoryClient = inventoryClient;
    }

    public OrderResponse createOrderWithSaga(CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);

        try {
            List<StockItemRequest> items = toStockItemRequests(order.items());
            inventoryClient.reserveStock(order.id(), items);

            log.info("Saga: stock reserved for order {}", order.id());
            return orderService.updateOrderStatus(order.id(), OrderStatus.STOCK_RESERVED);

        } catch (RestClientException ex) {
            log.warn("Saga: stock reservation failed for order {}; cancelling. Reason: {}",
                    order.id(), ex.getMessage());
            return orderService.updateOrderStatus(order.id(), OrderStatus.CANCELLED);
        }
    }

    private List<StockItemRequest> toStockItemRequests(List<OrderItemResponse> items) {
        return items.stream()
                .map(item -> new StockItemRequest(item.productId(), item.quantity()))
                .toList();
    }
}