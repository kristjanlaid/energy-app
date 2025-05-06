package com.enefit.backend.service;

import com.enefit.backend.dto.MonthlyCostDTO;
import com.enefit.backend.entity.Consumption;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.repository.ConsumptionRepository;
import com.enefit.backend.repository.MeteringPointRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConsumptionServiceTest {

    @Test
    void calculatesMonthlyCostCorrectly() {
        ConsumptionRepository consumptionRepository = mock(ConsumptionRepository.class);
        MeteringPointRepository meteringPointRepository = mock(MeteringPointRepository.class);
        PriceService priceService = mock(PriceService.class);
        ConsumptionService service = new ConsumptionService(consumptionRepository, meteringPointRepository, priceService);

        UUID meteringPointId = randomUUID();
        MeteringPoint point = new MeteringPoint(meteringPointId, "Test address", "Test name", null);

        LocalDateTime time = LocalDateTime.of(2025, 5, 1, 13, 0);
        Consumption c = new Consumption(randomUUID(), 10.0, time, point);

        when(meteringPointRepository.getReferenceById(meteringPointId)).thenReturn(point);
        when(consumptionRepository.findByMeteringPoint(point)).thenReturn(List.of(c));
        when(priceService.getPriceForHour(time)).thenReturn(0.1);

        List<MonthlyCostDTO> result = service.getMonthlyCost(meteringPointId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).month()).isEqualTo(5);
        assertThat(result.get(0).totalKwh()).isEqualTo(10.0);
        assertThat(result.get(0).totalCost()).isEqualTo(1.0);
    }
}
