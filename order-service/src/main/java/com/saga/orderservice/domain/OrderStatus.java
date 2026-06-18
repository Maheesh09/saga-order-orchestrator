package com.saga.orderservice.domain;

public enum OrderStatus {
    CREATED,
    STOCK_RESERVED,
    PAID,
    COMPLETED,
    CANCELLED
}