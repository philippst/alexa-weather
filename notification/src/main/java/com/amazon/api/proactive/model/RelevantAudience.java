package com.amazon.api.proactive.model;

public class RelevantAudience {

    private AudienceType type;
    private AudiencePayload payload;

    public AudienceType getType() {
        return type;
    }

    public void setType(AudienceType type) {
        this.type = type;
    }

    public AudiencePayload getPayload() {
        return payload;
    }

    public void setPayload(AudiencePayload payload) {
        this.payload = payload;
    }
}
