package com.saga.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(
        @NotNull Long productId,
        @NotBlank String name,
        @PositiveOrZero int totalQuantity
) {}