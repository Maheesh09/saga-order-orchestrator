package com.saga.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record OrderItemRequest(
        @NotNull Long productId,
        @Positive int quantity,
        @NotNull @Positive BigDecimal unitPrice
) {}