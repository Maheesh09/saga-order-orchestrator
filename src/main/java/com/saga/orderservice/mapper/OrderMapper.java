package com.saga.orderservice.mapper;

import com.saga.orderservice.domain.Order;
import com.saga.orderservice.domain.OrderItem;
import com.saga.orderservice.dto.CreateOrderRequest;
import com.saga.orderservice.dto.OrderItemResponse;
import com.saga.orderservice.dto.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(this::toItemResponse)
                        .toList()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }

    public Order toEntity(CreateOrderRequest request) {
        Order order = new Order(request.customerId());
        request.items().forEach(itemRequest -> {
            OrderItem item = new OrderItem(
                    itemRequest.productId(),
                    itemRequest.quantity(),
                    itemRequest.unitPrice()
            );
            order.addItem(item);
        });
        return order;
    }
}