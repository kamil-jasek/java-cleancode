package pl.sda.refactoring.service.domain;

import java.math.BigDecimal;

public record Price(BigDecimal value, Currency currency) {
}
