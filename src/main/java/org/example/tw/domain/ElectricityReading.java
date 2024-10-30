package org.example.tw.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @param reading kW
 */

public record ElectricityReading(Instant time, BigDecimal reading) {}

//@Data
//public class ElectricityReading{
//
//     LocalDateTime time;
//     BigDecimal reading;
//}
