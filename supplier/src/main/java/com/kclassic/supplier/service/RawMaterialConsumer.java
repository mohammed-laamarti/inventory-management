package com.kclassic.supplier.service;

import com.kclassic.supplier.dto.RawMaterialEvent;
import com.kclassic.supplier.entity.RawMaterialCache;
import com.kclassic.supplier.repository.RawMaterialCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RawMaterialConsumer {

    private final RawMaterialCacheRepository rawMaterialCacheRepository;

    /**
     * Écoute les événements de Raw Materials depuis raw-material-topic
     */
    @KafkaListener(
            topics = "raw-material-topic",
            groupId = "supplier-service-group"
    )
    public void consumeRawMaterialEvent(RawMaterialEvent event) {
        try {
            log.info("📥 Événement RawMaterial reçu: {} - {} - Type: {}",
                    event.getId(), event.getName(), event.getEventType());

            switch (event.getEventType()) {
                case "CREATED":
                    handleRawMaterialCreated(event);
                    break;
                case "UPDATED":
                    handleRawMaterialUpdated(event);
                    break;
                case "DELETED":
                    handleRawMaterialDeleted(event);
                    break;
                default:
                    log.warn("⚠️ Type d'événement inconnu: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("❌ Erreur traitement RawMaterial event {}: {}",
                    event.getId(), e.getMessage(), e);
        }
    }

    /**
     * Traite la création d'un nouveau Raw Material et le stocke dans le cache
     */
    private void handleRawMaterialCreated(RawMaterialEvent event) {
        log.info("✅ Création cache RawMaterial: {} - Prix: {} {} - Supplier: {}",
                event.getName(),
                event.getPrice(),
                event.getUnit(),
                event.getSupplierId());

        RawMaterialCache cache = new RawMaterialCache();
        cache.setRawMaterialId(event.getId());
        cache.setName(event.getName());
        cache.setPrice(event.getPrice());
        cache.setImageUrl(event.getImageUrl());
        cache.setUnit(event.getUnit());
        cache.setSupplierId(event.getSupplierId());
        cache.setUpdatedAt(event.getUpdatedAt());

        rawMaterialCacheRepository.save(cache);
        log.info("💾 Cache RawMaterial sauvegardé: {}", cache.getRawMaterialId());
    }

    /**
     * Traite la mise à jour d'un Raw Material dans le cache
     */
    private void handleRawMaterialUpdated(RawMaterialEvent event) {
        log.info("🔄 Mise à jour cache RawMaterial: {} - Nouveau prix: {} {}",
                event.getName(),
                event.getPrice(),
                event.getUnit());

        rawMaterialCacheRepository.findByRawMaterialId(event.getId())
                .ifPresentOrElse(
                        cache -> {
                            // Mettre à jour le cache existant
                            cache.setName(event.getName());
                            cache.setPrice(event.getPrice());
                            cache.setImageUrl(event.getImageUrl());
                            cache.setUnit(event.getUnit());
                            cache.setSupplierId(event.getSupplierId());
                            cache.setUpdatedAt(event.getUpdatedAt());
                            rawMaterialCacheRepository.save(cache);
                            log.info("💾 Cache RawMaterial mis à jour: {}", cache.getRawMaterialId());
                        },
                        () -> {
                            // Si le cache n'existe pas, le créer
                            log.warn("⚠️ Cache introuvable, création nouveau cache pour: {}", event.getId());
                            handleRawMaterialCreated(event);
                        }
                );
    }

    /**
     * Traite la suppression d'un Raw Material du cache
     */
    private void handleRawMaterialDeleted(RawMaterialEvent event) {
        log.info("🗑️ Suppression cache RawMaterial: {} - Supplier: {}",
                event.getId(),
                event.getSupplierId());

        rawMaterialCacheRepository.deleteByRawMaterialId(event.getId());
        log.info("💾 Cache RawMaterial supprimé: {}", event.getId());
    }
}