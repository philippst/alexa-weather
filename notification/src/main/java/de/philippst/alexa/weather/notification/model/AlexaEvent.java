package de.philippst.alexa.weather.notification.model;

import java.time.ZonedDateTime;

public class AlexaEvent {

    public AlexaEvent() {
    }

    public AlexaEvent(String referenceId, String userId, ZonedDateTime expires, ZonedDateTime created) {
        this.referenceId = referenceId;
        this.userId = userId;
        this.expires = expires;
        this.created = created;
    }

    private String referenceId;
    private String userId;
    private ZonedDateTime expires;
    private ZonedDateTime created;

    public ZonedDateTime getExpires() {
        return expires;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
