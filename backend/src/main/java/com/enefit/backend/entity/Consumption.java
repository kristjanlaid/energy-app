package com.enefit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Represents a single consumption reading recorded at a specific time
 * for a given metering point.
 */
@Entity
@Table(name="consumptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consumption {
    @Id
    @GeneratedValue
    private UUID id;

    private Double value; // Amount of energy consumed in kWh

    private LocalDateTime timestamp; //Timestamp when the consumption was recorded

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metering_point_id")
    private MeteringPoint meteringPoint;
}
