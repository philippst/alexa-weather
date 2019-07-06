package de.philippst.alexa.weather.notification.cap;

public enum Severity {

    UNKOWN(0),
    MINOR(10),
    MODERATE(20),
    SEVERE(30),
    EXTREME(50);

    private Integer priority;

    Severity(Integer priority) {
        this.priority = priority;

    }

    public Integer getPriority() {
        return priority;
    }

}
