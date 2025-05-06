package com.enefit.backend;

import com.enefit.backend.entity.Consumption;
import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.service.ConsumptionService;
import com.enefit.backend.service.CustomerService;
import com.enefit.backend.service.MeteringPointService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MeteringPointService meteringPointService;
    private final ConsumptionService consumptionService;

    public DataInitializer(CustomerService customerService,
                           BCryptPasswordEncoder passwordEncoder,
                           MeteringPointService meteringPointService,
                           ConsumptionService consumptionService, ConsumptionService consumptionService1) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.meteringPointService = meteringPointService;
        this.consumptionService = consumptionService1;
    }

    @Override
    public void run(String... args) throws Exception {
        if (customerService.findByUsername("user1").isEmpty() && customerService.findByUsername("user2").isEmpty()) {

            String hashed1 = passwordEncoder.encode("pass1");
            String hashed2 = passwordEncoder.encode("pass2");

            Customer c1 = customerService.save(new Customer(null, "user1", hashed1, "Test User"));
            Customer c2 = customerService.save(new Customer(null, "user2", hashed2, "Second User"));

            MeteringPoint mp1 = meteringPointService.save(new MeteringPoint(null, "Lelle 22", "Enefit", c1));
            MeteringPoint mp2 = meteringPointService.save(new MeteringPoint(null, "Suur-Sõjamäe tn 4", "Ülemiste Center", c1));
            MeteringPoint mp3 = meteringPointService.save(new MeteringPoint(null, "Kadaka tee 56a", "Kadaka Selver", c2));

            Random rand = new Random();
            LocalDateTime now = LocalDateTime.now();

            for (int i = 0; i < 12; i++) {
                LocalDateTime timestamp = now.minusMonths(i).withDayOfMonth(15).withHour(12).withMinute(0);
                double value1 = 10 + rand.nextDouble() * 15;
                double value2 = 5 + rand.nextDouble() * 20;

                consumptionService.save(new Consumption(null, value1, timestamp, mp1));
                consumptionService.save(new Consumption(null, value2, timestamp, mp2));
            }

            consumptionService.save(new Consumption(null, 13.5, now.minusDays(2), mp3));
            consumptionService.save(new Consumption(null, 15.2, now.minusDays(1), mp3));

            System.out.println("Test customers, metering points and full-year consumption data added!");
        }

    }
}
