package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.entity.OrderItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
final class OrderItemCurrencyExchanger {

    private final CurrencyService currencyService;

    List<OrderItem> exchangeCurrenciesInItems(@NonNull List<OrderItem> orderItems,
                                              @NonNull Currency baseCurrency) {
        return orderItems
            .stream()
            .map(item -> item.toBuilder()
                .id(UUID.randomUUID())
                .exchPrice(currencyService.exchange(item.price(), item.currency(), baseCurrency))
                .build())
            .collect(Collectors.toList());
    }
}