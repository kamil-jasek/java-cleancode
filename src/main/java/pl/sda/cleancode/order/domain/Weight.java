package pl.sda.cleancode.order.domain;

import lombok.NonNull;

import static pl.sda.cleancode.application.util.DomainArgumentCheck.check;

public record Weight(double value, @NonNull WeightUnit unit) {

    public Weight {
        check(value >= 0, "weight cannot be negative");
    }

    public static Weight of(double value, WeightUnit unit) {
        return new Weight(value, unit);
    }

    public boolean isHigherThen(Weight other) {
        return baseValue() > other.baseValue();
    }

    private double baseValue() {
        return unit.toGram(value);
    }

    public Weight multiply(int multiplicand) {
        return new Weight(value * multiplicand, unit);
    }

    public Weight plus(Weight other) {
        return new Weight(baseValue() + other.baseValue(), WeightUnit.GM);
    }
}
