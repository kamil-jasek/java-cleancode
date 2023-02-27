package pl.sda.cleancode.order.domain;

import lombok.NonNull;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static pl.sda.cleancode.application.util.DomainArgumentCheck.check;

public record Price(@NonNull BigDecimal value, @NonNull Currency currency) {
    public Price {
        check(value.compareTo(BigDecimal.ZERO) >= 0, "price cannot be negative");
    }

    public static Price of(String value, Currency currency) {
        return new Price(new BigDecimal(value), currency);
    }

    public Price multiply(BigDecimal multiplicand) {
        return new Price(value.multiply(multiplicand).setScale(2, HALF_UP), currency);
    }

    public Price plus(Price other) {
        checkCurrency(other);
        return new Price(value.add(other.value), currency);
    }

    public Price minus(Price other) {
        checkCurrency(other);
        return new Price(value.subtract(other.value), currency);
    }

    public Price exchangeTo(Currency currency, BigDecimal exchangeRate) {
        return new Price(value.multiply(exchangeRate).setScale(2, HALF_UP), currency);
    }

    public boolean isLowerThen(Price other) {
        checkCurrency(other);
        return value.compareTo(other.value) < 0;
    }

    public boolean isHigherThan(Price other) {
        checkCurrency(other);
        return value.compareTo(other.value) > 0;
    }

    private void checkCurrency(Price other) {
        check(currency.equals(other.currency), "price must be in the same currency");
    }
}
