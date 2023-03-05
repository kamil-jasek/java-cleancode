package pl.sda.refactoring.service.domain;

import java.util.List;

import static pl.sda.refactoring.util.Precondition.check;

public record ExchangedOrderItemList(
    List<ExchangedOrderItem> exchangedItems,
    Currency baseCurrency
) {
    public ExchangedOrderItemList {
        check(exchangedItems.stream()
                .allMatch(item -> item.exchangedPrice().currency().equals(baseCurrency)),
            "all exchanged prices must be in base currency: " + baseCurrency);
    }
}
