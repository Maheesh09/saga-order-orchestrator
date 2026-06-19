package com.saga.inventoryservice.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_reservations")
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected StockReservation() {
        // Required by JPA
    }

    public StockReservation(Long orderId, Long productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = ReservationStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public void markReleased() {
        if (status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Cannot release reservation " + id + "; current status is " + status);
        }
        status = ReservationStatus.RELEASED;
    }

    public void markConfirmed() {
        if (status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Cannot confirm reservation " + id + "; current status is " + status);
        }
        status = ReservationStatus.CONFIRMED;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}