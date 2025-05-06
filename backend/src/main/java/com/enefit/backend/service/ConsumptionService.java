package com.enefit.backend.service;

import com.enefit.backend.dto.CostEntryDTO;
import com.enefit.backend.dto.MonthlyCostDTO;
import com.enefit.backend.entity.Consumption;
import com.enefit.backend.repository.ConsumptionRepository;
import com.enefit.backend.repository.MeteringPointRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MeteringPointRepository meteringPointRepository;
    private final PriceService priceService;

    public ConsumptionService(ConsumptionRepository consumptionRepository, MeteringPointRepository meteringPointRepository, PriceService priceService) {
        this.consumptionRepository = consumptionRepository;
        this.meteringPointRepository = meteringPointRepository;
        this.priceService = priceService;
    }

    public List<Consumption> getByMeteringPointId(UUID meteringPointId) {
        return consumptionRepository.findByMeteringPoint(meteringPointRepository.getReferenceById(meteringPointId));
    }

    public Consumption save(Consumption c) {
        return consumptionRepository.save(c);
    }

    public List<CostEntryDTO> getCostEntries(UUID meteringPointId) {
        List<Consumption> consumptions = getByMeteringPointId(meteringPointId);

        return consumptions.stream()
                .map(c -> {
                    double price = priceService.getPriceForHour(c.getTimestamp());
                    double total = price * c.getValue();
                    return new CostEntryDTO(
                            c.getId(),
                            c.getTimestamp(),
                            c.getValue(),
                            price,
                            total
                    );
                })
                .toList();
    }

    public List<MonthlyCostDTO> getMonthlyCost(UUID meteringPointId) {
        List<Consumption> consumptions = getByMeteringPointId(meteringPointId);
        Map<Integer, List<Consumption>> byMonth = consumptions.stream()
                .collect(Collectors.groupingBy(c -> c.getTimestamp().getMonthValue()));

        List<MonthlyCostDTO> result = new ArrayList<>();

        for (Map.Entry<Integer, List<Consumption>> entry : byMonth.entrySet()) {
            int month = entry.getKey();
            List<Consumption> monthConsumptions = entry.getValue();

            double totalKwh = monthConsumptions.stream()
                    .mapToDouble(Consumption::getValue)
                    .sum();

            double totalCost = monthConsumptions.stream()
                    .mapToDouble(c -> c.getValue() * priceService.getPriceForHour(c.getTimestamp()))
                    .sum();

            result.add(new MonthlyCostDTO(month, totalKwh, totalCost));
        }

        result.sort(Comparator.comparingInt(MonthlyCostDTO::month));

        return result;
    }
}
