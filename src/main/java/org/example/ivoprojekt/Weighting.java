package org.example.ivoprojekt;

public class Weighting {
    private final String note;
    private final String todayWeightings;
    private final String weekWeightings;
    private final String specifiedPeriod;

    public Weighting(String note, String todayWeightings, String weekWeightings, String specifiedPeriod) {
        this.note = note;
        this.todayWeightings = todayWeightings;
        this.weekWeightings = weekWeightings;
        this.specifiedPeriod = specifiedPeriod;
    }

    public String getNote() {
        return note;
    }

    public String getTodayWeightings() {
        return todayWeightings;
    }

    public String getWeekWeightings() {
        return weekWeightings;
    }

    public String getSpecifiedPeriod() {
        return specifiedPeriod;
    }
}
