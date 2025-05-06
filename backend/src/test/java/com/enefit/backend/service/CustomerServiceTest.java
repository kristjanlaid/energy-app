package com.enefit.backend.service;

import com.enefit.backend.entity.Customer;
import com.enefit.backend.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        customerService = new CustomerService(customerRepository, passwordEncoder);
    }

    @Test
    void testFindByUsername() {
        String username = "john.doe";
        Customer mockCustomer = new Customer(UUID.randomUUID(), username, "hashedPass", "John Doe");

        when(customerRepository.findByUsername(username)).thenReturn(Optional.of(mockCustomer));

        Optional<Customer> result = customerService.findByUsername(username);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("john.doe");
        verify(customerRepository, times(1)).findByUsername(username);
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer(UUID.randomUUID(), "jane.doe", "pass123", "Jane Doe");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.save(customer);

        assertThat(result).isEqualTo(customer);
        verify(customerRepository, times(1)).save(customer);
    }
}
