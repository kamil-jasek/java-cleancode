package pl.sda.refactoring.service;

import pl.sda.refactoring.service.domain.Price;

interface DiscountPolicy {

    Price calculate(DiscountContext context);
}
