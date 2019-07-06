package de.philippst.alexa.weather.skill.util;

import com.github.filosganga.geogson.model.Point;

public class GeoUtils {

    /* Simple parser for WKT point string, e.g. 'POINT(123.123 456.789)' */
    public static Point wktToPoint(String wkt){
        if(wkt == null) return null;
        String[] parts = wkt.substring(6,wkt.length() -1).split(" ");
        return Point.from(Double.valueOf(parts[0]),Double.valueOf(parts[1]));
    }

}
