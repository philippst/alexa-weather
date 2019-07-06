package de.philippst.alexa.weather.common.model;

public class Position {

    public Position() {
    }

    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    private double lat;
    private double lng;
    private String locationName;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getBbox(){
        return lat+","+lng+","+lat+","+lng;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String toString() {
        return "lat="+lat+" lng="+lng;
    }
}