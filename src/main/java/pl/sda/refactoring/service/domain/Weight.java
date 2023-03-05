package pl.sda.refactoring.service.domain;

import static pl.sda.refactoring.service.domain.WeightUnit.GM;
import static pl.sda.refactoring.util.Precondition.check;

public record Weight(double value, WeightUnit unit) {

    public Weight {
        check(value >= 0, "weight must be positive");
    }

    public Weight add(Weight other) {
        return new Weight(unit.of(value) + other.unit.of(other.value), GM);
    }

    public boolean lt(Weight other) {
        return unit.of(value) < other.unit.of(other.value);
    }
}
