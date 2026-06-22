package com.saga.inventoryservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockReservationResultProducer {

    private static final Logger log = LoggerFactory.getLogger(StockReservationResultProducer.class);

    private final KafkaTemplate<String, StockReservationResult> kafkaTemplate;
    private final String resultTopic;

    public StockReservationResultProducer(
            KafkaTemplate<String, StockReservationResult> kafkaTemplate,
            @Value("${saga.topics.stock-reservation-result}") String resultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.resultTopic = resultTopic;
    }

    public void publishResult(StockReservationResult result) {
        log.info("Publishing StockReservationResult for order {}: {}", result.orderId(), result.outcome());

        kafkaTemplate.send(resultTopic, result.orderId().toString(), result);
    }
}