package com.saga.orderservice.dto;

import com.saga.orderservice.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerId,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {}