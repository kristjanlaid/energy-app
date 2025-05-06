package com.enefit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * A metering point (location) associated with a customer.
 */
@Entity
@Table(name="metering_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeteringPoint {

    @Id
    @GeneratedValue
    private UUID id;

    private String address;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
