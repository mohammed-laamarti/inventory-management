package com.kclassic.supplier.service;

import com.kclassic.supplier.dto.SupplierRequest;
import com.kclassic.supplier.dto.SupplierResponse;
import com.kclassic.supplier.entity.RawMaterialCache;
import com.kclassic.supplier.entity.Supplier;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupllierService {
    Page<Supplier> getSuppliers(int page, int size);
    SupplierResponse getSupplier(String id);
    SupplierResponse createSupplier(SupplierRequest supplier);
    void deleteSupplier(String id);
    SupplierResponse updateSupplier(String id,SupplierRequest supplier);
    List<RawMaterialCache> getSupplierRawMaterials(String supplierId);


}
