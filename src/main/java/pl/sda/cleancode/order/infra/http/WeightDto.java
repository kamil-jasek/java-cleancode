package pl.sda.cleancode.order.infra.http;

import jakarta.validation.constraints.Pattern;

record WeightDto(double value, @Pattern(regexp = "KG|GM|LB") String unit) {
}
