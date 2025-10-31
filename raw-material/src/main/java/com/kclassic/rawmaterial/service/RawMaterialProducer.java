package com.kclassic.rawmaterial.service;

import com.kclassic.rawmaterial.dto.RawMaterialEvent;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RawMaterialProducer {

    private final KafkaTemplate<String, RawMaterialEvent> kafkaTemplate;
    private static final String TOPIC = "raw-material-topic";

    public void sendRawMaterialCreatedEvent(RawMaterialResponse response) {
        RawMaterialEvent event = new RawMaterialEvent(
                response.getId(),
                response.getName(),
                response.getPrice(),
                response.getImageUrl(),
                response.getUnit(),
                response.getSupplierId(),
                response.getUpdatedAt()
        );
        sendEvent(event, "CREATED");
    }

    public void sendRawMaterialUpdatedEvent(RawMaterialResponse response) {
        RawMaterialEvent event = new RawMaterialEvent(
                response.getId(),
                response.getName(),
                response.getPrice(),
                response.getImageUrl(),
                response.getUnit(),
                response.getSupplierId(),
                response.getUpdatedAt()
        );
        sendEvent(event, "UPDATED");
    }

    public void sendRawMaterialDeletedEvent(UUID id, String supplierId) {
        RawMaterialEvent event = new RawMaterialEvent();
        event.setId(id);
        event.setSupplierId(supplierId);
        event.setUpdatedAt(java.time.LocalDateTime.now());
        sendEvent(event, "DELETED");
    }

    private void sendEvent(RawMaterialEvent event, String eventType) {
        try {
            event.setEventType(eventType);
            kafkaTemplate.send(TOPIC, event.getId().toString(), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("✅ RawMaterial {} event envoyé: {} - {}",
                                    eventType, event.getId(), event.getName());
                        } else {
                            log.error("❌ Erreur envoi RawMaterial {}: {}",
                                    eventType, ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("❌ Exception Kafka pour {}: {}", eventType, e.getMessage());
        }
    }
}