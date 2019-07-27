package de.philippst.alexa.weather.notification.model;

import de.philippst.alexa.weather.notification.cap.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class AlertLog {
    public AlertLog() {
    }

    private String id;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private String sender;
    private ZonedDateTime sent;
    private Status msgStatus;
    private MsgType msgType;
    private String msgSource;
    private Scope msgScope;
    private String msgCode;
    private Severity severity;
    private Urgency urgency;
    private Certainty certainty;
    private Category category;
    private String headline;
    private String event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ZonedDateTime getSent() {
        return sent;
    }

    public void setSent(ZonedDateTime sent) {
        this.sent = sent;
    }

    public Status getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Status msgStatus) {
        this.msgStatus = msgStatus;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(String msgSource) {
        this.msgSource = msgSource;
    }

    public Scope getMsgScope() {
        return msgScope;
    }

    public void setMsgScope(Scope msgScope) {
        this.msgScope = msgScope;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
