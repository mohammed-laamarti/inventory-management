package com.kclassic.supplier.controller;

import com.kclassic.supplier.dto.SupplierRequest;
import com.kclassic.supplier.dto.SupplierResponse;
import com.kclassic.supplier.entity.RawMaterialCache;
import com.kclassic.supplier.entity.Supplier;
import com.kclassic.supplier.service.SupllierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@Slf4j
public class SupplierController {

    private final SupllierService supplierService;

    public SupplierController(SupllierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<Page<Supplier>> getSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(supplierService.getSuppliers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable String id) {
        return ResponseEntity.ok(supplierService.getSupplier(id));
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable String id,
            @RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{supplierId}/raw-materials")
    public ResponseEntity<List<RawMaterialCache>> getSupplierRawMaterials(@PathVariable String supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierRawMaterials(supplierId));
    }
}