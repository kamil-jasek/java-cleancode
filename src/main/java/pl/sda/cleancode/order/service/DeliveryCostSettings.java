package pl.sda.cleancode.order.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import pl.sda.cleancode.order.domain.Price;
import pl.sda.cleancode.order.domain.Weight;

import java.math.BigDecimal;
import java.util.List;

import static pl.sda.cleancode.application.util.DomainArgumentCheck.check;

@ConfigurationProperties("orders.delivery-cost-settings")
record DeliveryCostSettings(Price defaultDeliveryCost, List<DeliveryCostLevel> deliveryCostLevels) {

    DeliveryCostSettings {
        boolean isDeliveryCostSorted = true;
        var prevPrice = new Price(BigDecimal.valueOf(Long.MAX_VALUE), defaultDeliveryCost.currency());
        for (final var deliveryCostLevel : deliveryCostLevels) {
            if (prevPrice.isLowerThen(deliveryCostLevel.minPrice)) {
                isDeliveryCostSorted = false;
                break;
            }
            prevPrice = deliveryCostLevel.minPrice;
        }
        check(isDeliveryCostSorted, "delivery cost level not sorted by min price");
    }

    record DeliveryCostLevel(
        Price minPrice,
        Weight maxWeight,
        Price deliveryCost
    ) {
        public boolean matches(Price price, Weight weight) {
            return minPrice.isLowerThen(price) && maxWeight.isHigherThen(weight);
        }
    }
}
