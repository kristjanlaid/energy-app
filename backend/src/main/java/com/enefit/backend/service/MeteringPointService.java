package com.enefit.backend.service;

import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.repository.CustomerRepository;
import com.enefit.backend.repository.MeteringPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for handling operations related to metering points.
 */
@Service
public class MeteringPointService {

    private final CustomerRepository customerRepository;
    private final MeteringPointRepository meteringPointRepository;

    public MeteringPointService(CustomerRepository customerRepository, MeteringPointRepository meteringPointRepository) {
        this.customerRepository = customerRepository;
        this.meteringPointRepository = meteringPointRepository;
    }

    public List<MeteringPoint> getMeteringPointsForUser(String username) {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        return customer.map(meteringPointRepository::findByCustomer).orElseGet(List::of);

    }

    public MeteringPoint save(MeteringPoint point) {
        return meteringPointRepository.save(point);
    }
}
