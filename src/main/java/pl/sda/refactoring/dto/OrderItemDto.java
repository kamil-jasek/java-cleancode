package pl.sda.refactoring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record OrderItemDto(
    @NotNull String productId,
    @NotNull BigDecimal price,
    @NotNull @Pattern(regexp = "USD|EUR|PLN") String currency,
    @NotNull Double weight,
    @NotNull @Pattern(regexp = "KG|GM|LB") String weightUnit,
    @NotNull @Min(1) Integer quantity
) {
}
