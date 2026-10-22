package com.logitrack.shipment.config;

import com.logitrack.shipment.util.EventTypes;
import org.apache.kafka.common.header.Header;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.nio.charset.StandardCharsets;

@Configuration
public class KafkaConfig {

    @Bean
    public RecordFilterStrategy<String, String> orderEventsFilter() {
        return record -> {
            Header header = record.headers().lastHeader("eventType");
            if (header == null) {
                return true;
            }
            String eventType = new String(header.value(), StandardCharsets.UTF_8);
            return !EventTypes.HANDLED.contains(eventType);
        };
    }
}
