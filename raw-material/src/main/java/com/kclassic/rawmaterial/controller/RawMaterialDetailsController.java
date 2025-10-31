package com.kclassic.rawmaterial.controller;


import com.kclassic.rawmaterial.dto.request.RawMaterialDetailsRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialDetailsResponse;
import com.kclassic.rawmaterial.service.RawMaterialDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/raw-material-details")
@RequiredArgsConstructor
public class RawMaterialDetailsController {

    private final RawMaterialDetailsService rawMaterialDetailsService;

    @GetMapping
    public ResponseEntity<List<RawMaterialDetailsResponse>> getAllRawMaterialDetails() {
        return ResponseEntity.ok(rawMaterialDetailsService.getRawMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialDetailsResponse> getRawMaterialDetailsById(@PathVariable UUID id) {
        return ResponseEntity.ok(rawMaterialDetailsService.getRawMaterial(id));
    }

    @PostMapping
    public ResponseEntity<RawMaterialDetailsResponse> createRawMaterialDetails(
            @RequestBody RawMaterialDetailsRequest request) {
        return ResponseEntity.ok(rawMaterialDetailsService.saveRawMaterial(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialDetailsResponse> updateRawMaterialDetails(
            @PathVariable UUID id,
            @RequestBody RawMaterialDetailsRequest request) {
        return ResponseEntity.ok(rawMaterialDetailsService.updateRawMaterial(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterialDetails(@PathVariable UUID id) {
        rawMaterialDetailsService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }
}

