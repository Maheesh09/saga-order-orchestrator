package com.saga.inventoryservice.controller;

import com.saga.inventoryservice.dto.ConfirmStockRequest;
import com.saga.inventoryservice.dto.ReleaseStockRequest;
import com.saga.inventoryservice.dto.ReservationResponse;
import com.saga.inventoryservice.dto.ReserveStockRequest;
import com.saga.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/reserve")
    public ReservationResponse reserveStock(@RequestBody @Valid ReserveStockRequest request) {
        return inventoryService.reserveStock(request);
    }

    @PostMapping("/release")
    public ReservationResponse releaseStock(@RequestBody @Valid ReleaseStockRequest request) {
        return inventoryService.releaseStock(request);
    }

    @PostMapping("/confirm")
    public ReservationResponse confirmStock(@RequestBody @Valid ConfirmStockRequest request) {
        return inventoryService.confirmStock(request);
    }
}