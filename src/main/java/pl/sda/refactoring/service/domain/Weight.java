package pl.sda.refactoring.service.domain;

import static pl.sda.refactoring.util.Precondition.check;

public record Weight(double value, WeightUnit unit) {

    public Weight {
        check(value >= 0, "weight must be positive");
    }
}
