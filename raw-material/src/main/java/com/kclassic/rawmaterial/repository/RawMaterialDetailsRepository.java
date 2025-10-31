package com.kclassic.rawmaterial.repository;

import com.kclassic.rawmaterial.entity.RawMaterialDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RawMaterialDetailsRepository extends JpaRepository<RawMaterialDetails, UUID> {
}
