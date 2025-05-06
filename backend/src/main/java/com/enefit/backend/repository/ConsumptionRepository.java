package com.enefit.backend.repository;

import com.enefit.backend.entity.Consumption;
import com.enefit.backend.entity.MeteringPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for accessing Consumption entities from the DB.
 */
public interface ConsumptionRepository extends JpaRepository<Consumption, UUID> {
    List<Consumption> findByMeteringPoint(MeteringPoint meteringPoint);
}
