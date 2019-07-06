package de.philippst.alexa.weather.notification.model;

import java.time.ZonedDateTime;

public class AlertLog {
    public AlertLog() {
    }

    public AlertLog(String id, ZonedDateTime timestamp, ZonedDateTime expires) {
        this.id = id;
        this.timestamp = timestamp;
        this.expires = expires;
    }

    private String id;
    private ZonedDateTime timestamp;
    private ZonedDateTime expires;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }
}
