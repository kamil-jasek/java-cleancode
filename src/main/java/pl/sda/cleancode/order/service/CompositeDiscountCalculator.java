package pl.sda.cleancode.order.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
final class CompositeDiscountCalculator implements DiscountCalculator {

    private final @NonNull List<DiscountCalculator> calculators;

    @Override
    public DiscountContext calculate(DiscountContext discountContext) {
        if (calculators.isEmpty()) {
            return discountContext;
        }
        for (final var calculator : calculators) {
            discountContext = calculator.calculate(discountContext);
        }
        return discountContext;
    }
}
