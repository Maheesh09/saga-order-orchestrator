package com.saga.inventoryservice.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(Long productId) {
        super("Product already exists with productId: " + productId);
    }
}