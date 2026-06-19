package com.saga.inventoryservice.dto;

public record ReservedItemResponse(
        Long productId,
        int quantity,
        int availableQuantity
) {}