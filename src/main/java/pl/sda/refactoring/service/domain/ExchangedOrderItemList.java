package pl.sda.refactoring.service.domain;

import lombok.NonNull;

import java.util.List;

import static pl.sda.refactoring.service.domain.WeightUnit.GM;
import static pl.sda.refactoring.util.Precondition.check;

public record ExchangedOrderItemList(
    @NonNull List<ExchangedOrderItem> exchangedItems,
    @NonNull Currency baseCurrency
) {
    public ExchangedOrderItemList {
        check(exchangedItems.stream()
                .allMatch(item -> item.exchangedPrice().currency().equals(baseCurrency)),
            "all exchanged prices must be in base currency: " + baseCurrency);
    }

    public Price totalPrice() {
        var result = Price.of("0.00", baseCurrency);
        for (final var item : exchangedItems) {
           result = result.add(item.fullExchangedPrice());
        }
        return result;
    }

    public Weight totalWeight() {
        var result = new Weight(0., GM);
        for (final var item : exchangedItems) {
            result = result.add(item.item().weight());
        }
        return result;
    }
}
