package pl.sda.refactoring.service.domain;

import static pl.sda.refactoring.util.Precondition.check;

public record Quantity(int value) {

    public Quantity {
        check(value >= 0, "must be eq/gt 0");
    }
}
