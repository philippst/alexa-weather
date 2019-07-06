package de.philippst.alexa.weather.common.dwd;

public enum Severity {

    MINOR(10,"Wetterwarnung","yellow", "gelb"),
    MODERATE(20,"Markante Wetterwarnung","orange", "orange"),
    SEVERE(30,"Unwetterwarnung","red", "rot"),
    EXTREME(50,"Extreme Unwetterwarnung","violet", "violett");

    private Integer priority;
    private String description;
    private String color;
    private String colorGerman;

    Severity(Integer priority, String description, String color, String colorGerman) {
        this.priority = priority;
        this.description = description;
        this.color = color;
        this.colorGerman = colorGerman;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getColorGerman() {
        return colorGerman;
    }

    public static Severity fromPriority(int priority){
        for (Severity sev : Severity.values()) {
            if (sev.priority.equals(priority)) {
                return sev;
            }
        }
        return null;
    }

}
