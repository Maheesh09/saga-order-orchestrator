package com.saga.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

public record ConfirmStockRequest(
        @NotNull Long orderId
) {}