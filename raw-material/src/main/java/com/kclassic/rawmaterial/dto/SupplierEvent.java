package com.kclassic.rawmaterial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SupplierEvent {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String eventType;
    private LocalDateTime timestamp;
    public SupplierEvent(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.eventType = "CREATED";
        this.timestamp = LocalDateTime.now();
    }
}
