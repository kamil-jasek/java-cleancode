package pl.sda.cleancode.order.infra.http;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

record OrderItemDto(
    @NotNull @UUID
    String productId,
    @NotNull
    PriceDto price,
    @NotNull
    WeightDto weight,
    @Min(1)
    int quantity
) {
}
