package com.enefit.backend.service;

import com.enefit.backend.external.EstfeedPriceRecord;
import com.enefit.backend.dto.PriceEntry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for retrieving electricity prices from the Elering API.
 * Prices are cached per day to minimize repeated API calls.
 */
@Service
public class PriceService {

    private final Map<LocalDate, List<PriceEntry>> cache = new ConcurrentHashMap<>();
    private final WebClient webClient = WebClient.create("https://estfeed.elering.ee");

    /**
     * Retrieves the price per kWh for a specific hour.
     * Cached prices for the day are used if available.
     */
    public double getPriceForHour(LocalDateTime timestamp) {
        LocalDate date = timestamp.toLocalDate();
        List<PriceEntry> prices = cache.computeIfAbsent(date, this::fetchPricesForDate);

        return prices.stream()
                .filter(p -> p.timestamp().toLocalDate().equals(timestamp.toLocalDate()))
                .map(PriceEntry::price)
                .findFirst()
                .orElse(-1.0); //fallback
    }

    /**
     * Fetches prices for a specific date from the Elering API.
     * Converts raw records to PriceEntry objects and stores them in the cache.
     */
    private List<PriceEntry> fetchPricesForDate(LocalDate date) {

        String start = date + "T00:00:00Z";
        String end = date.plusDays(1) + "T00:00:00Z";

        String url = "/api/public/v1/energy-price/electricity?startDateTime=" + start + "&endDateTime=" + end;

        try {
            List<EstfeedPriceRecord> rawRecords = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<EstfeedPriceRecord>>() {})
                    .block();

            if (rawRecords != null) {
                System.out.println("Fetched live prices for date: " + date);
                rawRecords.forEach(r -> System.out.println("  " + r.fromDateTime() + " -> " + r.centsPerKwh() + " cents"));
            }

            return rawRecords != null
                    ? rawRecords.stream()
                    .filter(r -> r.fromDateTime() != null)
                    .map(r -> new PriceEntry(
                            OffsetDateTime.parse(r.fromDateTime()).toLocalDateTime(),
                            r.centsPerKwh() / 100.0
                    ))
                    .toList()
                    : List.of();
        } catch (Exception e) {
            System.err.println("Failed to fetch prices: " + e.getMessage());
            return List.of();
        }
    }
}
