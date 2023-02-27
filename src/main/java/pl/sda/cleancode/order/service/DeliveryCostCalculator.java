package pl.sda.cleancode.order.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.cleancode.order.infra.port.ExchangeCurrencyPort;
import pl.sda.cleancode.order.service.DeliveryCostSettings.DeliveryCostLevel;
import pl.sda.cleancode.order.domain.ExchangedOrderItemList;
import pl.sda.cleancode.order.domain.Price;

@RequiredArgsConstructor
final class DeliveryCostCalculator {

    private final @NonNull DeliveryCostSettings settings;
    private final @NonNull ExchangeCurrencyPort currencyPort;

    Price calculate(@NonNull ExchangedOrderItemList orderItems) {
        final var totalPrice = orderItems.totalPrice();
        final var totalWeight = orderItems.totalWeight();
        return settings
            .deliveryCostLevels()
            .stream()
            .filter(level -> level.matches(totalPrice, totalWeight))
            .findFirst()
            .map(DeliveryCostLevel::deliveryCost)
            .orElseGet(() -> currencyPort
                .exchange(settings.defaultDeliveryCost(), orderItems.baseCurrency()));
    }
}
