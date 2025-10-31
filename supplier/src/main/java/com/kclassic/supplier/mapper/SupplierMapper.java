package com.kclassic.supplier.mapper;

import com.kclassic.supplier.dto.SupplierRequest;
import com.kclassic.supplier.dto.SupplierResponse;
import com.kclassic.supplier.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupplierMapper {

    /**
     * Convertit SupplierRequest en Supplier entity
     */
    public Supplier toSupplier(SupplierRequest request) {
        if (request == null) {
            return null;
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setBankAccount(request.getBankAccount());

        return supplier;
    }

    /**
     * Convertit Supplier entity en SupplierResponse
     */
    public SupplierResponse toSupplierResponse(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setEmail(supplier.getEmail());
        response.setPhone(supplier.getPhone());
        response.setAddress(supplier.getAddress());
        response.setBankAccount(supplier.getBankAccount());
        response.setUpdatedAt(supplier.getUpdatedAt());

        return response;
    }

    /**
     * Convertit une liste de Supplier entities en liste de SupplierResponse
     */
    public List<SupplierResponse> toSupplierResponseList(List<Supplier> suppliers) {
        if (suppliers == null) {
            return null;
        }

        return suppliers.stream()
                .map(this::toSupplierResponse)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un Supplier entity existant avec les données d'un SupplierRequest
     */
    public void updateSupplierFromRequest(SupplierRequest request, Supplier supplier) {
        if (request == null || supplier == null) {
            return;
        }

        if (request.getName() != null) {
            supplier.setName(request.getName());
        }
        if (request.getEmail() != null) {
            supplier.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            supplier.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            supplier.setAddress(request.getAddress());
        }
        if (request.getBankAccount() != null) {
            supplier.setBankAccount(request.getBankAccount());
        }
    }
}
