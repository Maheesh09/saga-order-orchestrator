package com.saga.inventoryservice.mapper;

import com.saga.inventoryservice.domain.Product;
import com.saga.inventoryservice.domain.StockReservation;
import com.saga.inventoryservice.dto.ProductResponse;
import com.saga.inventoryservice.dto.ReservationResponse;
import com.saga.inventoryservice.dto.ReservedItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryMapper {

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getTotalQuantity(),
                product.getReservedQuantity(),
                product.getAvailableQuantity()
        );
    }

    public ReservedItemResponse toReservedItemResponse(StockReservation reservation, Product product) {
        return new ReservedItemResponse(
                reservation.getProductId(),
                reservation.getQuantity(),
                product.getAvailableQuantity()
        );
    }

    public ReservationResponse toReservationResponse(Long orderId, List<ReservedItemResponse> items) {
        return new ReservationResponse(orderId, items);
    }
}