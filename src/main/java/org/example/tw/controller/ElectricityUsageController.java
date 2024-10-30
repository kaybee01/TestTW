package org.example.tw.controller;


import org.example.tw.service.AccountService;
import org.example.tw.service.ElectricityUsageService;
import org.example.tw.service.PricePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/usage")
public class ElectricityUsageController {

    private final ElectricityUsageService electricityUsageService;

    @Autowired
    public ElectricityUsageController(ElectricityUsageService electricityUsageService) {
        this.electricityUsageService = electricityUsageService;
    }

    @GetMapping("/weekly-cost/{smartMeterId}")
    public ResponseEntity<BigDecimal> getWeeklyUsageCost(@PathVariable String smartMeterId) {
        try {
            BigDecimal weeklyCost = electricityUsageService.calculateWeeklyUsageCost(smartMeterId);
            return ResponseEntity.ok(weeklyCost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(BigDecimal.ZERO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BigDecimal.ZERO);
        }
    }
}
