package com.kclassic.supplier.service;

import com.kclassic.supplier.dto.SupplierEvent;
import com.kclassic.supplier.dto.SupplierResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class SupplierProducer {

    private final KafkaTemplate<String, SupplierEvent> kafkaTemplate;
    private static final String TOPIC = "supplier-topic";

    public SupplierProducer(KafkaTemplate<String, SupplierEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSupplierCreatedEvent(SupplierResponse response) {
        log.info("🚀 Préparation envoi événement CREATED pour supplier: {}", response.getId());
        SupplierEvent event = new SupplierEvent(
                response.getId(),
                response.getName(),
                response.getEmail(),
                response.getPhone()
        );
        sendEvent(event, "CREATED");
    }

    public void sendSupplierUpdatedEvent(SupplierResponse response) {
        log.info("🚀 Préparation envoi événement UPDATED pour supplier: {}", response.getId());
        SupplierEvent event = new SupplierEvent(
                response.getId(),
                response.getName(),
                response.getEmail(),
                response.getPhone()
        );
        sendEvent(event, "UPDATED");
    }

    public void sendSupplierDeletedEvent(String supplierId) {
        log.info("🚀 Préparation envoi événement DELETED pour supplier: {}", supplierId);
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
            event.setEventType(eventType);

            log.info("📤 Envoi vers Kafka - Topic: {}, Key: {}, EventType: {}",
                    TOPIC, event.getId(), eventType);
            log.debug("📤 Contenu événement: {}", event);

            CompletableFuture<SendResult<String, SupplierEvent>> future =
                    kafkaTemplate.send(TOPIC, event.getId(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("✅ Supplier {} event envoyé avec succès: {} - Partition: {}, Offset: {}",
                            eventType,
                            event.getId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("❌ Erreur envoi Supplier {} pour {}: {}",
                            eventType, event.getId(), ex.getMessage(), ex);
                }
            });
        } catch (Exception e) {
            log.error("❌ Exception lors de l'envoi Kafka pour {}: {}", eventType, e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'envoi de l'événement Kafka", e);
        }
    }
}