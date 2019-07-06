package de.philippst.alexa.weather.common.model;

import de.philippst.alexa.weather.common.dwd.Severity;

import java.time.LocalDate;
import java.util.Date;

public class AlexaUser {

    public AlexaUser(String userId) {
        this.userId = userId;
    }

    private String userId;
    private Date lastUpdated;
    private String countryCode;
    private String postalCode;
    private Double lat;
    private Double lng;
    private String locationName;
    private String deviceId;
    private boolean permissionPostalcode;
    private boolean permissionNotification;
    private boolean subscriptionWeatherAlert;
    private LocalDate lastNotificationHint;
    private int notificationLevel = 30;
    private Severity severity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean getPermissionPostalcode() {
        return permissionPostalcode;
    }

    public void setPermissionPostalcode(boolean permissionPostalcode) {
        this.permissionPostalcode = permissionPostalcode;
    }

    public boolean getPermissionNotification() {
        return permissionNotification;
    }

    public void setPermissionNotification(boolean permissionNotification) {
        this.permissionNotification = permissionNotification;
    }

    public boolean getSubscriptionWeatherAlert() {
        return subscriptionWeatherAlert;
    }

    public void setSubscriptionWeatherAlert(boolean subscriptionWeatherAlert) {
        this.subscriptionWeatherAlert = subscriptionWeatherAlert;
    }

    public LocalDate getLastNotificationHint() {
        return lastNotificationHint;
    }

    public void setLastNotificationHint(LocalDate lastNotificationHint) {
        this.lastNotificationHint = lastNotificationHint;
    }

    public int getNotificationLevel() {
        return notificationLevel;
    }

    public void setNotificationLevel(int notificationLevel) {
        this.notificationLevel = notificationLevel;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
