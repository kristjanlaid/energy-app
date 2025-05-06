package com.enefit.backend.service;

import com.enefit.backend.entity.Customer;
import com.enefit.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for handling customer operations
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    /**
     * Saves a customer to the database.
     * Note: Password should be encoded before calling this method
     */
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
