package de.philippst.alexa.weather.notification.cap;

import java.util.List;

public class GeocodeArea extends Area{

    private List<Geocode> geocode;

    public List<Geocode> getGeocode() {
        return geocode;
    }

    public void setGeocode(List<Geocode> geocode) {
        this.geocode = geocode;
    }
}
