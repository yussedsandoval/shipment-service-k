package com.logitrack.shipment.util;

import java.util.Set;

public final class EventTypes {

    public static final String ORDER_CREATED = "ORDER_CREATED";
    public static final String ORDER_CANCELLED = "ORDER_CANCELLED";

    public static final Set<String> HANDLED = Set.of(ORDER_CREATED, ORDER_CANCELLED);

    private EventTypes() {
    }
}
