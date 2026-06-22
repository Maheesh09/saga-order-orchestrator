package com.saga.orderservice.event; // (or com.saga.inventoryservice.event)

public record ReservationItem(
        Long productId,
        int quantity
) {}