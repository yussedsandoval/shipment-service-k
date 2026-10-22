package com.logitrack.shipment.service;

import com.logitrack.shipment.dto.OrderCanceledPayload;
import com.logitrack.shipment.dto.OrderCreatedPayload;
import com.logitrack.shipment.dto.ShipmentResponse;
import com.logitrack.shipment.entity.Shipment;
import com.logitrack.shipment.repository.IShipmentRepository;
import com.logitrack.shipment.util.ShipmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShipmentService {

    private final IShipmentRepository shipmentRepository;

    public ShipmentService(IShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    public void createShipment(OrderCreatedPayload payload) {

        if (shipmentRepository.existsByOrderId(payload.orderId())) {
            log.info("Shipment for order {} already exists, skipping", payload.orderId());
            return;
        }
        Shipment shipment = new Shipment(payload.orderId(), payload.customerId(), payload.deliveryAddress());
        shipmentRepository.save(shipment);
        log.info("Shipment {} created for order {}", shipment.getId(), payload.orderId());
    }

    @Transactional
    public void cancelShipment(OrderCanceledPayload payload) {
        Optional<Shipment> optionalShipment = shipmentRepository.findByOrderId(payload.orderId());
        if (optionalShipment.isEmpty()) {
            log.warn("No shipment found for canceled order {}", payload.orderId());
            return;
        }
        Shipment shipment = optionalShipment.get();
        if (shipment.getStatus() == ShipmentStatus.CANCELED) {
            log.info("Shipment for order {} already canceled, skipping", payload.orderId());
            return;
        }
        if (shipment.getStatus() != ShipmentStatus.PENDING) {
            log.warn("Shipment for order {} is {}, cannot be canceled", payload.orderId(), shipment.getStatus());
            return;
        }

        shipment.cancelShipment();
        log.info("Shipment {} canceled for order {}", shipment.getId(), payload.orderId());
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponse> getShipments() {
        return shipmentRepository.findAll().stream()
                .map(ShipmentResponse::from)
                .toList();
    }
}
