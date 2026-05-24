package org.example.ivoprojekt.controller.utill;

public enum WeighingTimePeriods {
    TODAY("Dnešné váženia"),
    WEEK("Tento týždeň"),
    CUSTOM("Zadané obdobie");

    public final String period;

    WeighingTimePeriods(String period) {
        this.period = period;
    }

    public static WeighingTimePeriods fromString(String period) {
        for (WeighingTimePeriods p : WeighingTimePeriods.values()) {
            if (p.period.equals(period)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown period: " + period);
    }
}
