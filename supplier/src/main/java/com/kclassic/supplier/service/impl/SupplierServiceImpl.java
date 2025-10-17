package com.kclassic.supplier.service.impl;

import com.kclassic.supplier.dto.SupplierRequest;
import com.kclassic.supplier.dto.SupplierResponse;
import com.kclassic.supplier.entity.Supplier;
import com.kclassic.supplier.mapper.SupplierMapper;
import com.kclassic.supplier.repository.SupplierRepository;
import com.kclassic.supplier.service.SupllierService;
import com.kclassic.supplier.service.SupplierProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SupplierServiceImpl implements SupllierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper mapper;
    private final SupplierProducer producer;

    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               SupplierMapper mapper,
                               SupplierProducer producer) {
        this.supplierRepository = supplierRepository;
        this.mapper = mapper;
        this.producer = producer;
    }

    @Override
    public Page<Supplier> getSuppliers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return supplierRepository.findAll(pageable);
    }

    @Override
    public SupplierResponse getSupplier(String id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        return mapper.toSupplierResponse(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = mapper.toSupplier(supplierRequest);
        supplier = supplierRepository.save(supplier);

        SupplierResponse response = mapper.toSupplierResponse(supplier);

        try {
            producer.sendSupplierCreatedEvent(response);
            log.info("Supplier créé et événement Kafka envoyé: {}", response.getId());
        } catch (Exception e) {
            log.error("Supplier créé mais erreur Kafka: {}", e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(String id, SupplierRequest supplierRequest) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        existingSupplier.setName(supplierRequest.getName());
        existingSupplier.setAddress(supplierRequest.getAddress());
        existingSupplier.setPhone(supplierRequest.getPhone());
        existingSupplier.setEmail(supplierRequest.getEmail());
        existingSupplier.setBankAccount(supplierRequest.getBankAccount());

        existingSupplier = supplierRepository.save(existingSupplier);

        SupplierResponse response = mapper.toSupplierResponse(existingSupplier);

        try {
            producer.sendSupplierUpdatedEvent(response);
            log.info("Supplier mis à jour et événement Kafka envoyé: {}", id);
        } catch (Exception e) {
            log.error("Supplier mis à jour mais erreur Kafka: {}", e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional
    public void deleteSupplier(String id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Supplier not found with id: " + id);
        }

        supplierRepository.deleteById(id);

        try {
            producer.sendSupplierDeletedEvent(id);
            log.info("Supplier supprimé et événement Kafka envoyé: {}", id);
        } catch (Exception e) {
            log.error("Supplier supprimé mais erreur Kafka: {}", e.getMessage());
        }
    }


}