package com.kclassic.rawmaterial.controller;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import com.kclassic.rawmaterial.entity.SupplierCache;
import com.kclassic.rawmaterial.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public ResponseEntity<Page<RawMaterialResponse>> getAllRawMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RawMaterialResponse> rawMaterials = rawMaterialService.getRawMaterials(page, size);
        return ResponseEntity.ok(rawMaterials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> getRawMaterial(@PathVariable UUID id) {
        RawMaterialResponse response = rawMaterialService.getRawMaterial(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RawMaterialResponse> createRawMaterial(@RequestBody RawMaterialRequest request) {
        RawMaterialResponse response = rawMaterialService.saveRawMaterial(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> updateRawMaterial(
            @PathVariable UUID id,
            @RequestBody RawMaterialRequest request) {
        RawMaterialResponse response = rawMaterialService.updateRawMaterial(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable UUID id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierCache>> getSuppliers() {
        return ResponseEntity.ok(rawMaterialService.getSuppliers());
    }
}
