package com.enefit.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for exposing consumption data to clients.
 */

public record ConsumptionDTO(UUID id, double value, LocalDateTime timestamp, UUID meteringPointId) {
}
