package pl.sda.cleancode.order.domain;

public record Quantity(int value) {
    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
