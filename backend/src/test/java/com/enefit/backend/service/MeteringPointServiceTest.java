package com.enefit.backend.service;

import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.repository.CustomerRepository;
import com.enefit.backend.repository.MeteringPointRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MeteringPointServiceTest {

    @Test
    void returnsMeteringPointsForExistingUser() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        MeteringPointRepository meteringPointRepository = mock(MeteringPointRepository.class);
        MeteringPointService service = new MeteringPointService(customerRepository, meteringPointRepository);

        String username = "john";
        Customer customer = new Customer(randomUUID(), username, "pwd", "John Doe");
        List<MeteringPoint> expected = List.of(new MeteringPoint(randomUUID(), "123 Main St", "Home", customer));

        when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));
        when(meteringPointRepository.findByCustomer(customer)).thenReturn(expected);

        List<MeteringPoint> result = service.getMeteringPointsForUser(username);

        assertThat(result).isEqualTo(expected);
        verify(customerRepository, times(1)).findByUsername(username);
        verify(meteringPointRepository, times(1)).findByCustomer(customer);
    }
}
