package com.kclassic.rawmaterial.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCache {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
}