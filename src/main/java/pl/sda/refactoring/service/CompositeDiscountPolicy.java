package pl.sda.refactoring.service;

import lombok.NonNull;
import pl.sda.refactoring.service.domain.Price;

import java.util.List;

import static pl.sda.refactoring.util.Precondition.check;

final class CompositeDiscountPolicy implements DiscountPolicy {

    private final List<DiscountPolicy> discountPolicies;

    CompositeDiscountPolicy(@NonNull List<DiscountPolicy> discountPolicies) {
        check(!discountPolicies.isEmpty(), "empty policies");
        this.discountPolicies = discountPolicies;
    }

    @Override
    public Price calculate(DiscountContext context) {
        var result = Price.of("0.00", context.currentDiscount().currency());
        for (final var policy : discountPolicies) {
            result = policy.calculate(context.withCurrentPrice(result));
        }
        return result;
    }
}
