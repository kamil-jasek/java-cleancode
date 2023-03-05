package pl.sda.refactoring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

public record OrderItemDto(
    @NotNull @UUID String productId,
    @NotNull @Min(0) BigDecimal price,
    @NotNull @Pattern(regexp = "USD|EUR|PLN") String currency,
    @NotNull @Min(0) BigDecimal weight,
    @NotNull @Pattern(regexp = "KG|GM|LB") String weightUnit,
    @NotNull @Min(1) Integer quantity
) {
}
