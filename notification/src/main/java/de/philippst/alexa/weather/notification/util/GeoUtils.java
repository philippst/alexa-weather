package de.philippst.alexa.weather.notification.util;

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

}
