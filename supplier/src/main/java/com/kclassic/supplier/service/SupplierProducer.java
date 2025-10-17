package com.kclassic.supplier.service;

import com.kclassic.supplier.dto.SupplierEvent;
import com.kclassic.supplier.dto.SupplierResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SupplierProducer {

    private final KafkaTemplate<String, SupplierEvent> kafkaTemplate;
    private static final String TOPIC = "supplier-topic";

    public SupplierProducer(KafkaTemplate<String, SupplierEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSupplierCreatedEvent(SupplierResponse response) {
        SupplierEvent event = new SupplierEvent(
                response.getId(),
                response.getName(),
                response.getEmail(),
                response.getPhone()
        );
        sendEvent(event, "CREATED");
    }

    public void sendSupplierUpdatedEvent(SupplierResponse response) {
        SupplierEvent event = new SupplierEvent(
                response.getId(),
                response.getName(),
                response.getEmail(),
                response.getPhone()
        );
        sendEvent(event, "UPDATED");
    }

    public void sendSupplierDeletedEvent(String supplierId) {
        SupplierEvent event = new SupplierEvent(
                supplierId,
                null,
                null,
                null
        );
        sendEvent(event, "DELETED");
    }

    private void sendEvent(SupplierEvent event, String eventType) {
        try {
            kafkaTemplate.send(TOPIC, event.getId(), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("✅ Supplier {} event envoyé: {}", eventType, event.getId());
                        } else {
                            log.error("❌ Erreur envoi Supplier {}: {}", eventType, ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("❌ Exception Kafka pour {}: {}", eventType, e.getMessage());
        }
    }
}