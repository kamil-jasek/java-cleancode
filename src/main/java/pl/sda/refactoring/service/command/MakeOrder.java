package pl.sda.refactoring.service.command;

import lombok.NonNull;
import pl.sda.refactoring.service.EmptyOrderItemsListException;
import pl.sda.refactoring.service.domain.Currency;
import pl.sda.refactoring.service.domain.CustomerId;
import pl.sda.refactoring.service.domain.OrderItem;

import java.util.List;

public record MakeOrder(
    @NonNull CustomerId customerId,
    @NonNull List<OrderItem> orderItems,
    @NonNull Currency baseCurrency,
    String coupon
) {

    public MakeOrder {
        if (orderItems.isEmpty()) {
            throw new EmptyOrderItemsListException();
        }
    }
}