package com.enefit.backend.external;

/**
 * Represents a single price record returned by the Estfeed API.
 *
 * The timestamps are strings because the Elering API
 * returns them in offset format (OffsetDateTime) and they're parsed later when needed.
 */
public record EstfeedPriceRecord(
        double centsPerKwh,
        String fromDateTime,
        String toDateTime
        ) {}
