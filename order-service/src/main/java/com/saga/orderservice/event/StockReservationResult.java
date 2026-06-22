package com.saga.orderservice.event;

public record StockReservationResult(
        Long orderId,
        StockReservationOutcome outcome,
        String reason
) {}