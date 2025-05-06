package com.enefit.backend.dto;

/**
 * DTO for representing total consumption and cost for a given month.
 */

public record MonthlyCostDTO(int month, double totalKwh, double totalCost) {
}
