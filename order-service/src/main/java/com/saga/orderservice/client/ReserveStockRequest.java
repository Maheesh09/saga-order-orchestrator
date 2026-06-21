package com.saga.orderservice.client;

import java.util.List;

public record ReserveStockRequest(
        Long orderId,
        List<StockItemRequest> items
) {}