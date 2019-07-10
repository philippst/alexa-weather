package de.philippst.alexa.weather.notification.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = RelevantAudiencePayload.Builder.class)
public final class RelevantAudiencePayload {

    public static Builder builder(){
        return new Builder();
    }

    private String user;

    private RelevantAudiencePayload(Builder builder) {
        this.user = builder.user;
    }

    public String getUser() {
        return user;
    }

    public static final class Builder {
        private String user;

        public Builder() {
        }

        public Builder withUser(String val) {
            user = val;
            return this;
        }

        public RelevantAudiencePayload build() {
            return new RelevantAudiencePayload(this);
        }
    }
}
