package com.saga.orderservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class RestInventoryClient implements InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(RestInventoryClient.class);

    private final RestClient restClient;

    public RestInventoryClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void reserveStock(Long orderId, List<StockItemRequest> items) {
        log.info("Calling Inventory Service to reserve stock for order {}", orderId);

        ReserveStockRequest request = new ReserveStockRequest(orderId, items);

        restClient.post()
                .uri("/inventory/reserve")
                .body(request)
                .retrieve()
                .toBodilessEntity();

        log.info("Stock reservation succeeded for order {}", orderId);
    }
}