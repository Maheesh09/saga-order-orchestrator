package com.saga.orderservice.saga;

import com.saga.orderservice.dto.CreateOrderRequest;
import com.saga.orderservice.dto.OrderItemResponse;
import com.saga.orderservice.dto.OrderResponse;
import com.saga.orderservice.event.ReservationItem;
import com.saga.orderservice.event.StockReservationEventProducer;
import com.saga.orderservice.event.StockReservationRequested;
import com.saga.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(SagaOrchestrator.class);

    private final OrderService orderService;
    private final StockReservationEventProducer eventProducer;

    public SagaOrchestrator(OrderService orderService, StockReservationEventProducer eventProducer) {
        this.orderService = orderService;
        this.eventProducer = eventProducer;
    }

    public OrderResponse createOrderWithSaga(CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);

        List<ReservationItem> items = toReservationItems(order.items());
        eventProducer.publishReservationRequested(new StockReservationRequested(order.id(), items));

        log.info("Saga: reservation requested for order {}; awaiting result", order.id());

        return order;
    }

    private List<ReservationItem> toReservationItems(List<OrderItemResponse> items) {
        return items.stream()
                .map(item -> new ReservationItem(item.productId(), item.quantity()))
                .toList();
    }
}