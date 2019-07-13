package de.philippst.alexa.weather.common.dwd;

public enum Severity {

    MINOR(10,"Wetterwarnung","yellow", "gelb",1),
    MODERATE(20,"Markante Wetterwarnung","orange", "orange",2),
    SEVERE(30,"Unwetterwarnung","red", "rot",3),
    EXTREME(40,"Extreme Unwetterwarnung","violet", "violett",4);

    private Integer priority;
    private String description;
    private String color;
    private String colorGerman;
    private Integer level;

    Severity(Integer priority, String description, String color, String colorGerman, Integer level) {
        this.priority = priority;
        this.description = description;
        this.color = color;
        this.colorGerman = colorGerman;
        this.level = level;
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

    public Integer getLevel() {
        return level;
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
