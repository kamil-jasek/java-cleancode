package pl.sda.refactoring.service.domain;

public record ExchangedOrderItem(OrderItem item, Price exchangedPrice) {
}
