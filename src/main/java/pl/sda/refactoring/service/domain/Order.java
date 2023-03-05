package pl.sda.refactoring.service.domain;

public record Order(
    OrderId orderId,
    CreateTime createTime,
    OrderStatus status,
    CustomerId customerId,
    ExchangedOrderItemList exchangedItems,
    Price discountPrice,
    Price deliveryPrice
) {
}
