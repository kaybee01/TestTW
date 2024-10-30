package org.example.tw.service;

import org.example.tw.domain.ElectricityReading;
import org.example.tw.domain.PricePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElectricityUsageService {

    private final MeterReadingService meterReadingService;
    private final PricePlanService pricePlanService;
    private final AccountService accountService;

    @Autowired
    public ElectricityUsageService(MeterReadingService meterReadingService, PricePlanService pricePlanService,AccountService accountService) {
        this.meterReadingService = meterReadingService;
        this.pricePlanService = pricePlanService;
        this.accountService=accountService;
    }

    public BigDecimal calculateWeeklyUsageCost(String smartMeterId) {
        // Fetch the readings
        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
                .orElseThrow(() -> new IllegalArgumentException("No readings found for smart meter ID: " + smartMeterId));

        // Check if readings are available after resolving the Optional
        if (readings.isEmpty()) {
            throw new IllegalArgumentException("No readings available for smart meter ID: " + smartMeterId);
        }

        // Implement the logic to calculate weekly usage cost
        Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        List<ElectricityReading> weeklyReadings = readings.stream()
                .filter(reading -> reading.time().isAfter((oneWeekAgo)))
                .collect(Collectors.toList());

        if (weeklyReadings.isEmpty()) {
            throw new IllegalArgumentException("No readings available in the past week for smart meter ID: " + smartMeterId);
        }

        BigDecimal totalReading = weeklyReadings.stream()
                .map(ElectricityReading::reading)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageReading = totalReading.divide(BigDecimal.valueOf(weeklyReadings.size()), RoundingMode.HALF_UP);
        BigDecimal hoursInWeek = BigDecimal.valueOf(168); // 7 days * 24 hours

        // Assuming a fixed unit rate from PricePlan for simplicity
        BigDecimal unitRate = pricePlanService.getPricePlanBySmartMeterId(smartMeterId).getUnitRate();
        BigDecimal energyConsumed = averageReading.multiply(hoursInWeek);

        return energyConsumed.multiply(unitRate);
    }

}

