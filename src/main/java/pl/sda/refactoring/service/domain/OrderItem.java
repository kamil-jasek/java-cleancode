package pl.sda.refactoring.service.domain;

public record OrderItem(
    ProductId productId,
    Price price,
    Weight weight,
    Quantity quantity
) {
}
