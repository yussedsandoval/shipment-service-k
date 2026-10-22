package com.logitrack.shipment.repository;

import com.logitrack.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IShipmentRepository extends JpaRepository<Shipment, UUID> {

    boolean existsByOrderId(UUID orderId);

    Optional<Shipment> findByOrderId(UUID orderId);
}
