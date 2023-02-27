package pl.sda.cleancode.order.domain;

public enum WeightUnit {
    KG(1000),
    GM(1),
    LB(453.59237);

    private final double grams;

    WeightUnit(double grams) {
        this.grams = grams;
    }

    public double toGram(double value) {
        return value * grams;
    }
}
