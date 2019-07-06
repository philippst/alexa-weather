package de.philippst.alexa.weather.notification.cap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

    private Severity severity;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Area> area;
    private ZonedDateTime expires;
    private ZonedDateTime effective;
    private ZonedDateTime onset;
    private Urgency urgency;
    private Certainty certainty;
    private String description;
    private String language;
    private Category category;
    private String event;
    private String headline;
    private ResponseType responseType;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<EventCode> eventCode;

    public List<EventCode> getEventCode() {
        return eventCode;
    }

    public void setEventCode(List<EventCode> eventCode) {
        this.eventCode = eventCode;
    }

    public List<Area> getArea() {
        return area;
    }

    public void setArea(List<Area> area) {
        this.area = area;
    }

    public ZonedDateTime getEffective() {
        return effective;
    }

    public void setEffective(ZonedDateTime effective) {
        this.effective = effective;
    }

    public ZonedDateTime getOnset() {
        return onset;
    }

    public void setOnset(ZonedDateTime onset) {
        this.onset = onset;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public Certainty getCertainty() {
        return certainty;
    }

    public void setCertainty(Certainty certainty) {
        this.certainty = certainty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
}
