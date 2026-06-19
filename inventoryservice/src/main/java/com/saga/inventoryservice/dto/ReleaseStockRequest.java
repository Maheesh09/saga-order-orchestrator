package com.saga.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

public record ReleaseStockRequest(
        @NotNull Long orderId
) {}