package org.example.tw.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @param reading kW
 */
public record ElectricityReading(Instant time, BigDecimal reading) {}
