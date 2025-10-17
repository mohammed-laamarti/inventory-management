package com.kclassic.rawmaterial.service;

import com.kclassic.rawmaterial.dto.SupplierEvent;
import com.kclassic.rawmaterial.entity.SupplierCache;
import com.kclassic.rawmaterial.repository.SupplierCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierConsumer {

    private final SupplierCacheRepository repository;

    @KafkaListener(topics = "supplier-topic", groupId = "rawmaterial-group")
    public void consume(SupplierEvent event) {
        SupplierCache cache = new SupplierCache(
                event.getId(),
                event.getName(),
                event.getEmail(),
                event.getPhone()
        );
        System.out.println("Consumed SupplierEvent: " + event);
        repository.save(cache);
    }
}

