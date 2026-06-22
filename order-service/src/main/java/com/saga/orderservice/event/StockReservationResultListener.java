package com.saga.orderservice.event;

import com.saga.orderservice.domain.OrderStatus;
import com.saga.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockReservationResultListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservationResultListener.class);

    private final OrderService orderService;

    public StockReservationResultListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${saga.topics.stock-reservation-result}")
    public void handleReservationResult(StockReservationResult result) {
        log.info("Received StockReservationResult for order {}: {}", result.orderId(), result.outcome());

        OrderStatus newStatus = switch (result.outcome()) {
            case RESERVED -> OrderStatus.STOCK_RESERVED;
            case FAILED -> OrderStatus.CANCELLED;
        };

        orderService.updateOrderStatus(result.orderId(), newStatus);

        if (result.outcome() == StockReservationOutcome.FAILED) {
            log.warn("Order {} cancelled: {}", result.orderId(), result.reason());
        }
    }
}