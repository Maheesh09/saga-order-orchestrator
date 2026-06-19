package com.saga.inventoryservice.repository;

import com.saga.inventoryservice.domain.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {

    List<StockReservation> findByOrderId(Long orderId);
}