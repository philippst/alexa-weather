package de.philippst.alexa.weather.common.util;

import com.github.filosganga.geogson.model.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

import java.util.ArrayList;
import java.util.List;

public class GeoUtils {

    public static String polygonToWkt(Polygon polygon){
        WKTWriter wktWriter = new WKTWriter();
        return wktWriter.write(polygon);
    }

    public static Polygon xmlStringToPolygon(String xmlString){
        GeometryFactory geometryFactory = new GeometryFactory();

        List<Coordinate> coordinateList = new ArrayList<>();
        String[] points = xmlString.split(" ");
        for(String point : points){
            String[] pointCoord = point.split(",");
            coordinateList.add(new Coordinate(Double.valueOf(pointCoord[1]),Double.valueOf(pointCoord[0])));
        }
        return geometryFactory.createPolygon(coordinateList.toArray(new Coordinate[0]));
    }

    /* Simple parser for WKT point string, e.g. 'POINT(123.123 456.789)' */
    public static Point wktToPoint(String wkt){
        if(wkt == null) return null;
        String[] parts = wkt.substring(6,wkt.length() -1).split(" ");
        return Point.from(Double.valueOf(parts[0]),Double.valueOf(parts[1]));
    }

}
