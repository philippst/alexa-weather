package de.philippst.alexa.weather.notification.model;

import org.locationtech.jts.geom.Point;

import java.time.ZonedDateTime;
import java.util.List;

public class AlexaUser {

    private String id;
    private List<AlexaEvent> events;
    private String countryCode;
    private String deviceId;
    private ZonedDateTime lastUpdated;
    private String locationName;
    private String postalCode;
    private Point position;
    private boolean permissionPostalcode;
    private boolean permissionNotification;
    private boolean subscriptionWeatherAlert;
    private int notificationLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public List<AlexaEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AlexaEvent> events) {
        this.events = events;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean isPermissionPostalcode() {
        return permissionPostalcode;
    }

    public void setPermissionPostalcode(boolean permissionPostalcode) {
        this.permissionPostalcode = permissionPostalcode;
    }

    public boolean isPermissionNotification() {
        return permissionNotification;
    }

    public void setPermissionNotification(boolean permissionNotification) {
        this.permissionNotification = permissionNotification;
    }

    public boolean isSubscriptionWeatherAlert() {
        return subscriptionWeatherAlert;
    }

    public void setSubscriptionWeatherAlert(boolean subscriptionWeatherAlert) {
        this.subscriptionWeatherAlert = subscriptionWeatherAlert;
    }

    public int getNotificationLevel() {
        return notificationLevel;
    }

    public void setNotificationLevel(int notificationLevel) {
        this.notificationLevel = notificationLevel;
    }
}
