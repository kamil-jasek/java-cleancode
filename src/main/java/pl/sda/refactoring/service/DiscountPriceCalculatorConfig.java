package pl.sda.refactoring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.refactoring.service.port.DiscountPort;

import java.time.Clock;
import java.util.List;

@Configuration
class DiscountPriceCalculatorConfig {

    @Bean
    DiscountPriceCalculator discountPriceCalculator(DiscountPort discountPort,
                                                    OrderSettings settings,
                                                    Clock clock) {
        return new DiscountPriceCalculator(new CompositeDiscountPolicy(
            List.of(
                new CouponDiscountPolicy(discountPort),
                new FreeDeliveryDayDiscountPolicy(settings, clock)
            )
        ));
    }
}
