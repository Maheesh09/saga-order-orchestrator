package com.saga.orderservice.event;

import java.util.List;

public record StockReservationRequested(
        Long orderId,
        List<ReservationItem> items
) {}