package com.saga.inventoryservice.event;

import com.saga.inventoryservice.dto.ReserveStockRequest;
import com.saga.inventoryservice.dto.StockItemRequest;
import com.saga.inventoryservice.exception.InsufficientStockException;
import com.saga.inventoryservice.exception.ProductNotFoundException;
import com.saga.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockReservationRequestListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservationRequestListener.class);

    private final InventoryService inventoryService;
    private final StockReservationResultProducer resultProducer;

    public StockReservationRequestListener(InventoryService inventoryService,
                                           StockReservationResultProducer resultProducer) {
        this.inventoryService = inventoryService;
        this.resultProducer = resultProducer;
    }

    @KafkaListener(topics = "${saga.topics.stock-reservation-requested}")
    public void handleReservationRequested(StockReservationRequested event) {
        log.info("Received StockReservationRequested for order {}", event.orderId());

        try {
            ReserveStockRequest request = toReserveStockRequest(event);
            inventoryService.reserveStock(request);

            log.info("Reservation succeeded for order {}", event.orderId());
            resultProducer.publishResult(
                    new StockReservationResult(event.orderId(), StockReservationOutcome.RESERVED, null));

        } catch (InsufficientStockException | ProductNotFoundException ex) {
            log.warn("Reservation failed for order {}: {}", event.orderId(), ex.getMessage());
            resultProducer.publishResult(
                    new StockReservationResult(event.orderId(), StockReservationOutcome.FAILED, ex.getMessage()));
        }
    }

    private ReserveStockRequest toReserveStockRequest(StockReservationRequested event) {
        List<StockItemRequest> items = event.items().stream()
                .map(item -> new StockItemRequest(item.productId(), item.quantity()))
                .toList();
        return new ReserveStockRequest(event.orderId(), items);
    }
}