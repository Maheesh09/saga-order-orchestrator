package com.saga.orderservice.client;

public record StockItemRequest(
        Long productId,
        int quantity
) {}