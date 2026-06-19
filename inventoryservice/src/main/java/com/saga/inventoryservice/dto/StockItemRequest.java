package com.saga.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockItemRequest(
        @NotNull Long productId,
        @Positive int quantity
) {}