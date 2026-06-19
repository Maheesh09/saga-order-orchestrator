package com.saga.inventoryservice.dto;

public record ProductResponse(
        Long productId,
        String name,
        int totalQuantity,
        int reservedQuantity,
        int availableQuantity
) {}