package com.logitrack.shipment.listener;

import com.logitrack.shipment.dto.EventEnvelope;
import com.logitrack.shipment.dto.OrderCanceledPayload;
import com.logitrack.shipment.dto.OrderCreatedPayload;
import com.logitrack.shipment.service.ShipmentService;
import com.logitrack.shipment.util.EventTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OrderEventListener {

    private final ShipmentService shipmentService;
    private final ObjectMapper objectMapper;

    public OrderEventListener(ShipmentService shipmentService, ObjectMapper objectMapper) {
        this.shipmentService = shipmentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "logistics.events", filter = "orderEventsFilter")
    public void onEvent(ConsumerRecord<String, String> record) {
        String eventType = eventType(record);
        log.info("Received {} key={} partition={} offset={}",
                eventType, record.key(), record.partition(), record.offset());

        switch (eventType) {
            case EventTypes.ORDER_CREATED -> {
                EventEnvelope<OrderCreatedPayload> event = objectMapper.readValue(
                        record.value(), new TypeReference<EventEnvelope<OrderCreatedPayload>>() {});
                shipmentService.createShipment(event.payload());
            }
            case EventTypes.ORDER_CANCELLED -> {
                EventEnvelope<OrderCanceledPayload> event = objectMapper.readValue(
                        record.value(), new TypeReference<EventEnvelope<OrderCanceledPayload>>() {});
                shipmentService.cancelShipment(event.payload());
            }
            default -> log.warn("Unhandled event type {}", eventType);
        }
    }

    private String eventType(ConsumerRecord<String, String> record) {
        Header header = record.headers().lastHeader("eventType");
        return new String(header.value(), StandardCharsets.UTF_8);
    }
}
