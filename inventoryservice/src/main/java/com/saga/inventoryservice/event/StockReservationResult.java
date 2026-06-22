package com.saga.inventoryservice.event;

public record StockReservationResult(
        Long orderId,
        StockReservationOutcome outcome,
        String reason
) {}