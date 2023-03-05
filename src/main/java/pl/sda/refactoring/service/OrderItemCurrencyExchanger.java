package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.entity.OrderItem;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
final class OrderItemCurrencyExchanger {

    private final CurrencyExchangerPort currencyExchanger;

    List<OrderItem> exchangeCurrenciesInItems(@NonNull List<OrderItem> orderItems,
                                              @NonNull Currency baseCurrency) {
        return orderItems
            .stream()
            .map(item -> item.toBuilder()
                .id(UUID.randomUUID())
                .exchPrice(currencyExchanger.exchange(item.price(), item.currency(), baseCurrency))
                .build())
            .collect(Collectors.toList());
    }
}