package com.enefit.backend.controller;

import com.enefit.backend.dto.ConsumptionDTO;
import com.enefit.backend.dto.CostEntryDTO;
import com.enefit.backend.dto.MonthlyCostDTO;
import com.enefit.backend.entity.Consumption;
import com.enefit.backend.service.ConsumptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that provides endpoints to retrieve electricity consumption data and costs.
 */
@RestController
@RequestMapping("/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    /**
     * Retrieves all consumption entries for a given metering point.
     */
    @GetMapping
    public ResponseEntity<List<ConsumptionDTO>> getByMeteringPoint(@RequestParam UUID meteringPointId) {
        List<Consumption> consumptions = consumptionService.getByMeteringPointId(meteringPointId);

        List<ConsumptionDTO> result = consumptions.stream()
                .map(c -> new ConsumptionDTO(
                        c.getId(),
                        c.getValue(),
                        c.getTimestamp(),
                        c.getMeteringPoint().getId()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves hourly cost breakdown for a given metering point based on consumption and live price data.
     */
    @GetMapping("/cost")
    public ResponseEntity<List<CostEntryDTO>> getCost(@RequestParam UUID meteringPointId) {
        List<CostEntryDTO> costEntries = consumptionService.getCostEntries(meteringPointId);
        return ResponseEntity.ok(costEntries);
    }

    /**
     * Retrieves aggregated monthly consumption costs for a given metering point.
     */
    @GetMapping("/cost/monthly")
    public ResponseEntity<List<MonthlyCostDTO>> getMonthlyCost(@RequestParam UUID meteringPointId) {
        List<MonthlyCostDTO> result = consumptionService.getMonthlyCost(meteringPointId);
        return ResponseEntity.ok(result);
    }
}
