package pl.sda.cleancode.order.infra.http;

import jakarta.validation.constraints.Pattern;

record PriceDto(
    @Pattern(regexp = "\\d+\\.\\d{2}") String value,
    @Pattern(regexp = "USD|PLN|EUR") String currency) {
}
