package com.saga.inventoryservice.domain;

import com.saga.inventoryservice.exception.InsufficientStockException;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = false)
    private int reservedQuantity;

    protected Product() {
        // Required by JPA
    }

    public Product(Long productId, String name, int totalQuantity) {
        this.productId = productId;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = 0;
    }

    public void reserve(int quantity) {
        int availableQuantity = totalQuantity - reservedQuantity;
        if (quantity > availableQuantity) {
            throw new InsufficientStockException(productId, quantity, availableQuantity);
        }
        reservedQuantity += quantity;
    }

    public void release(int quantity) {
        if (quantity > reservedQuantity) {
            throw new IllegalStateException(
                    "Cannot release " + quantity + " units for product " + productId
                            + "; only " + reservedQuantity + " are reserved");
        }
        reservedQuantity -= quantity;
    }

    public void confirmDeduction(int quantity) {
        if (quantity > reservedQuantity) {
            throw new IllegalStateException(
                    "Cannot confirm deduction of " + quantity + " units for product " + productId
                            + "; only " + reservedQuantity + " are reserved");
        }
        reservedQuantity -= quantity;
        totalQuantity -= quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public int getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }
}