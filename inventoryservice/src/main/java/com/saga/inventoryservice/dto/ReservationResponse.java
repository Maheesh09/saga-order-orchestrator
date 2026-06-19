package com.saga.inventoryservice.dto;

import java.util.List;

public record ReservationResponse(
        Long orderId,
        List<ReservedItemResponse> items
) {}