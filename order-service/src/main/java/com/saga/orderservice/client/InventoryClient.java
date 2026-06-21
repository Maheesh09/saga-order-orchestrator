package com.saga.orderservice.client;

import java.util.List;

public interface InventoryClient {

    void reserveStock(Long orderId, List<StockItemRequest> items);
}