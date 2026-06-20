package com.saga.inventoryservice.service;

import com.saga.inventoryservice.domain.Product;
import com.saga.inventoryservice.domain.StockReservation;
import com.saga.inventoryservice.dto.*;
import com.saga.inventoryservice.exception.ProductAlreadyExistsException;
import com.saga.inventoryservice.exception.ProductNotFoundException;
import com.saga.inventoryservice.mapper.InventoryMapper;
import com.saga.inventoryservice.repository.ProductRepository;
import com.saga.inventoryservice.repository.StockReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final ProductRepository productRepository;
    private final StockReservationRepository stockReservationRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryService(ProductRepository productRepository,
                            StockReservationRepository stockReservationRepository,
                            InventoryMapper inventoryMapper) {
        this.productRepository = productRepository;
        this.stockReservationRepository = stockReservationRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating product {}", request.productId());

        if (productRepository.findByProductId(request.productId()).isPresent()) {
            throw new ProductAlreadyExistsException(request.productId());
        }

        Product product = new Product(request.productId(), request.name(), request.totalQuantity());
        Product savedProduct = productRepository.save(product);

        log.info("Product {} created with initial stock {}",
                savedProduct.getProductId(), savedProduct.getTotalQuantity());

        return inventoryMapper.toProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProducts(Long productId){
        log.info("Fetching product {}", productId);

        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return inventoryMapper.toProductResponse(product);
    }

    @Transactional
    public ReservationResponse reserveStock(ReserveStockRequest request) {
        log.info("Reserving stock for order {}", request.orderId());

        List<ReservedItemResponse> itemResponses = request.items().stream()
                .map(item -> reserveSingleItem(request.orderId(), item))
                .toList();

        log.info("Stock reserved for order {}", request.orderId());

        return inventoryMapper.toReservationResponse(request.orderId(), itemResponses);
    }

    private ReservedItemResponse reserveSingleItem(Long orderId, StockItemRequest item) {
        Product product = productRepository.findByProductId(item.productId())
                .orElseThrow(() -> new ProductNotFoundException(item.productId()));

        product.reserve(item.quantity());
        productRepository.save(product);

        StockReservation reservation = new StockReservation(orderId, item.productId(), item.quantity());
        stockReservationRepository.save(reservation);

        return inventoryMapper.toReservedItemResponse(reservation, product);
    }

    @Transactional
    public ReservationResponse releaseStock(ReleaseStockRequest request) {
        log.info("Releasing stock for order {}", request.orderId());

        List<StockReservation> reservations = stockReservationRepository.findByOrderId(request.orderId());

        List<ReservedItemResponse> itemResponses = reservations.stream()
                .map(this::releaseSingleReservation)
                .toList();

        log.info("Stock released for order {}", request.orderId());

        return inventoryMapper.toReservationResponse(request.orderId(), itemResponses);
    }

    private ReservedItemResponse releaseSingleReservation(StockReservation reservation) {
        Product product = productRepository.findByProductId(reservation.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(reservation.getProductId()));

        product.release(reservation.getQuantity());
        reservation.markReleased();

        productRepository.save(product);
        stockReservationRepository.save(reservation);

        return inventoryMapper.toReservedItemResponse(reservation, product);
    }

    @Transactional
    public ReservationResponse confirmStock(ConfirmStockRequest request) {
        log.info("Confirming stock deduction for order {}", request.orderId());

        List<StockReservation> reservations = stockReservationRepository.findByOrderId(request.orderId());

        List<ReservedItemResponse> itemResponses = reservations.stream()
                .map(this::confirmSingleReservation)
                .toList();

        log.info("Stock deduction confirmed for order {}", request.orderId());

        return inventoryMapper.toReservationResponse(request.orderId(), itemResponses);
    }

    private ReservedItemResponse confirmSingleReservation(StockReservation reservation) {
        Product product = productRepository.findByProductId(reservation.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(reservation.getProductId()));

        product.confirmDeduction(reservation.getQuantity());
        reservation.markConfirmed();

        productRepository.save(product);
        stockReservationRepository.save(reservation);

        return inventoryMapper.toReservedItemResponse(reservation, product);
    }
}