package com.kclassic.rawmaterial.service;

import com.kclassic.rawmaterial.dto.SupplierEvent;
import com.kclassic.rawmaterial.entity.SupplierCache;
import com.kclassic.rawmaterial.repository.SupplierCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierConsumer {

    private final SupplierCacheRepository repository;

    @KafkaListener(
            topics = "supplier-topic",
            groupId = "rawmaterial-group"
    )
    @Transactional
    public void consume(
            @Payload SupplierEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            log.info("📥 ============================================");
            log.info("📥 Événement Kafka reçu sur partition: {}, offset: {}", partition, offset);
            log.info("📥 Supplier ID: {}, Name: {}, EventType: {}",
                    event.getId(), event.getName(), event.getEventType());
            log.debug("📥 Contenu complet: {}", event);

            if (event.getEventType() == null) {
                log.warn("⚠️ EventType null, événement ignoré: {}", event.getId());
                return;
            }

            switch (event.getEventType().toUpperCase()) {
                case "CREATED":
                    handleSupplierCreated(event);
                    break;
                case "UPDATED":
                    handleSupplierUpdated(event);
                    break;
                case "DELETED":
                    handleSupplierDeleted(event);
                    break;
                default:
                    log.warn("⚠️ Type d'événement inconnu: {}", event.getEventType());
            }

            log.info("📥 Traitement terminé pour supplier: {}", event.getId());
            log.info("📥 ============================================");

        } catch (Exception e) {
            log.error("❌ ============================================");
            log.error("❌ ERREUR critique lors du traitement de l'événement");
            log.error("❌ Supplier ID: {}, EventType: {}", event.getId(), event.getEventType());
            log.error("❌ Message d'erreur: {}", e.getMessage(), e);
            log.error("❌ ============================================");
            throw new RuntimeException("Erreur traitement événement Supplier", e);
        }
    }

    /**
     * Création d'un nouveau Supplier dans le cache
     */
    private void handleSupplierCreated(SupplierEvent event) {
        log.info("✅ Traitement événement CREATED pour: {} - {}", event.getId(), event.getName());

        // Vérifier si le cache existe déjà
        if (repository.existsById(event.getId())) {
            log.warn("⚠️ Cache supplier {} existe déjà, mise à jour au lieu de création", event.getId());
            handleSupplierUpdated(event);
            return;
        }

        SupplierCache cache = new SupplierCache(
                event.getId(),
                event.getName(),
                event.getEmail(),
                event.getPhone()
        );

        SupplierCache saved = repository.save(cache);
        log.info("💾 Cache Supplier créé avec succès - ID: {}, Name: {}", saved.getId(), saved.getName());
    }

    /**
     * Mise à jour d'un Supplier dans le cache
     */
    private void handleSupplierUpdated(SupplierEvent event) {
        log.info("🔄 Traitement événement UPDATED pour: {} - {}", event.getId(), event.getName());

        repository.findById(event.getId())
                .ifPresentOrElse(
                        cache -> {
                            log.info("📝 Mise à jour cache existant: {}", event.getId());
                            cache.setName(event.getName());
                            cache.setEmail(event.getEmail());
                            cache.setPhone(event.getPhone());
                            SupplierCache updated = repository.save(cache);
                            log.info("💾 Cache Supplier mis à jour - ID: {}, Name: {}",
                                    updated.getId(), updated.getName());
                        },
                        () -> {
                            log.warn("⚠️ Cache introuvable pour update, création nouveau cache: {}",
                                    event.getId());
                            handleSupplierCreated(event);
                        }
                );
    }

    /**
     * Suppression d'un Supplier du cache
     */
    private void handleSupplierDeleted(SupplierEvent event) {
        log.info("🗑️ Traitement événement DELETED pour: {}", event.getId());

        if (!repository.existsById(event.getId())) {
            log.warn("⚠️ Cache {} n'existe pas, suppression ignorée", event.getId());
            return;
        }

        repository.deleteById(event.getId());
        log.info("💾 Cache Supplier supprimé avec succès: {}", event.getId());
    }
}