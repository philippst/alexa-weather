package com.amazon.api.proactive.model;

public class AudiencePayload {

    public AudiencePayload(String user) {
        this.user = user;
    }

    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
