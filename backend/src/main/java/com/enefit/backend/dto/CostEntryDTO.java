package com.enefit.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * DTO for representing the calculated cost of a single consumption entry.
 */

public record CostEntryDTO(
        UUID id,
        LocalDateTime timestamp,
        double value,
        double pricePerUnit,
        double totalCost
) {}
