package de.philippst.alexa.weather.notification.cap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {

    private String areaDesc;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Polygon> polygon;
    private String altitude;
    private String ceiling;

    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public List<Polygon> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Polygon> polygon) {
        this.polygon = polygon;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }
}
