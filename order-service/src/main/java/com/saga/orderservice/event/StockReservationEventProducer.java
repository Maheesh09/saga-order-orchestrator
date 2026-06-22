package com.saga.orderservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockReservationEventProducer {

    private static final Logger log = LoggerFactory.getLogger(StockReservationEventProducer.class);

    private final KafkaTemplate<String, StockReservationRequested> kafkaTemplate;
    private final String requestTopic;

    public StockReservationEventProducer(
            KafkaTemplate<String, StockReservationRequested> kafkaTemplate,
            @Value("${saga.topics.stock-reservation-requested}") String requestTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.requestTopic = requestTopic;
    }

    public void publishReservationRequested(StockReservationRequested event) {
        log.info("Publishing StockReservationRequested for order {}", event.orderId());

        kafkaTemplate.send(requestTopic, event.orderId().toString(), event);
    }
}