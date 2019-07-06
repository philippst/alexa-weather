package de.philippst.alexa.weather.common.dwd;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Alert implements Comparable<Alert>{

    private String identifier;
    private String sender;
    private String sent;
    private String status;
    private String msgType;
    private String source;
    private String scope;
    private String lanuage;
    private String category;
    private String event;
    private String urgency;
    private String headline;
    private String description;
    private String instruction;
    private Severity severity;
    private ZonedDateTime effective;
    private ZonedDateTime onset;
    private ZonedDateTime expires;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getLanuage() {
        return lanuage;
    }

    public void setLanuage(String lanuage) {
        this.lanuage = lanuage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public ZonedDateTime getExpires() {
        return expires;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    public int compareTo(Alert o) {
        return Integer.compare(o.getSeverity().getPriority(),this.getSeverity().getPriority());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.identifier);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }
        Alert other = (Alert) obj;
        return other.getIdentifier().equals(this.getIdentifier());
    }

}
