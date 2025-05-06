package com.enefit.backend.repository;

import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeteringPointRepository extends JpaRepository<MeteringPoint, UUID> {
    List<MeteringPoint> findByCustomer(Customer customer);
}
