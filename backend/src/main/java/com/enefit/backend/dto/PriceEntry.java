package com.enefit.backend.dto;

import java.time.LocalDateTime;

/**
 * Represents a single electricity price entry for a given hour.
 */
public record PriceEntry(LocalDateTime timestamp, double price) {}
