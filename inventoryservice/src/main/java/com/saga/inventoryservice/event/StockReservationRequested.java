package com.saga.inventoryservice.event;

import java.util.List;

public record StockReservationRequested(
        Long orderId,
        List<ReservationItem> items
) {}