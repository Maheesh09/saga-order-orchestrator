package com.saga.inventoryservice.event; // (or com.saga.inventoryservice.event)

public record ReservationItem(
        Long productId,
        int quantity
) {}