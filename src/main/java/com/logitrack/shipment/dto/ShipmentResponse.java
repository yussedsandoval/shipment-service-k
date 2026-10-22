package com.logitrack.shipment.dto;

import com.logitrack.shipment.entity.Shipment;
import com.logitrack.shipment.util.ShipmentStatus;

import java.time.Instant;
import java.util.UUID;

public record ShipmentResponse(
        UUID id,
        UUID orderId,
        UUID customerId,
        String deliveryAddress,
        ShipmentStatus status,
        UUID courierId,
        Instant createdAt,
        Instant updatedAt) {

    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getCustomerId(),
                shipment.getDeliveryAddress(),
                shipment.getStatus(),
                shipment.getCourierId(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt()
        );
    }
}
