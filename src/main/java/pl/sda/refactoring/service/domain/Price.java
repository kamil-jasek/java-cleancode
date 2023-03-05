package pl.sda.refactoring.service.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.HALF_UP;
import static pl.sda.refactoring.util.Precondition.check;

public record Price(BigDecimal value, Currency currency) {

    public static Price of(String value, Currency currency) {
        return new Price(new BigDecimal(value), currency);
    }

    public Price add(Price other) {
        check(currency.equals(other.currency), "must be in the same currency");
        return new Price(value.add(other.value), currency);
    }

    public boolean gt(Price other) {
        check(currency.equals(other.currency), "must be in the same currency");
        return value.compareTo(other.value) > 0;
    }

    public Price multiply(BigDecimal multiplier) {
        return new Price(value.multiply(multiplier).setScale(2, HALF_UP), currency);
    }

    public Price exchangeTo(BigDecimal multiplicand, Currency target) {
        return new Price(value.multiply(multiplicand).setScale(2, HALF_UP), target);
    }

    public Price minus(Price other) {
        check(currency.equals(other.currency), "must be in the same currency");
        return new Price(value.subtract(other.value), currency);
    }
}
