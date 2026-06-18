package com.saga.orderservice.service;

import com.saga.orderservice.domain.Order;
import com.saga.orderservice.dto.CreateOrderRequest;
import com.saga.orderservice.dto.OrderResponse;
import com.saga.orderservice.exception.OrderNotFoundException;
import com.saga.orderservice.mapper.OrderMapper;
import com.saga.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer {}", request.customerId());

        Order order = orderMapper.toEntity(request);
        Order savedOrder = orderRepository.save(order);

        log.info("Order {} created for customer {}", savedOrder.getId(), request.customerId());

        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");

        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}