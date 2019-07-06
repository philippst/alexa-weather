package com.amazon.api.proactive.model;

import com.amazon.api.proactive.model.schema.EventSchema;
import com.amazon.api.proactive.model.schema.LocalizedAttribute;

import java.time.ZonedDateTime;
import java.util.List;

public class ProactiveEvent {

    private ZonedDateTime timestamp;
    private String referenceId;
    private ZonedDateTime expiryTime;
    private EventSchema event;
    private List<LocalizedAttribute> localizedAttributes;
    private RelevantAudience relevantAudience;

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public EventSchema getEvent() {
        return event;
    }

    public void setEvent(EventSchema event) {
        this.event = event;
    }

    public List<LocalizedAttribute> getLocalizedAttributes() {
        return localizedAttributes;
    }

    public void setLocalizedAttributes(List<LocalizedAttribute> localizedAttributes) {
        this.localizedAttributes = localizedAttributes;
    }

    public RelevantAudience getRelevantAudience() {
        return relevantAudience;
    }

    public void setRelevantAudience(RelevantAudience relevantAudience) {
        this.relevantAudience = relevantAudience;
    }

}
