package org.example.tw.domain;

import java.util.List;

public record MeterReadings(String smartMeterId, List<ElectricityReading> electricityReadings) {}
