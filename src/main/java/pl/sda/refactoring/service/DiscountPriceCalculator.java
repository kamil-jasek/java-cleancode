package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.refactoring.service.domain.Price;

@RequiredArgsConstructor
class DiscountPriceCalculator {
    private final @NonNull DiscountPolicy discountPolicy;

    Price calculateDiscountPrice(DiscountContext ctx) {
        return discountPolicy.calculate(ctx);
    }
}