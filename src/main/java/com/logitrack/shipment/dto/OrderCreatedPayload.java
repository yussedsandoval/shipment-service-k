package com.logitrack.shipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.UUID;

// items are ignored: shipment only needs where the order goes, not what it contains
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCreatedPayload(
        UUID orderId,
        UUID customerId,
        String deliveryAddress,
        Instant createdAt) {
}
