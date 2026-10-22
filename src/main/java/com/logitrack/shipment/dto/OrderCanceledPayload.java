package com.logitrack.shipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCanceledPayload(
        UUID orderId,
        UUID customerId,
        UUID canceledBy,
        Instant canceledAt) {
}
