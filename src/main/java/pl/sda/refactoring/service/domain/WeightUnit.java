package pl.sda.refactoring.service.domain;

public enum WeightUnit {
    KG(1000), LB(453.59237), GM(1);

    private final double value;

    WeightUnit(double value) {
        this.value = value;
    }

    public double of(double i) {
        return i * value;
    }
}
