package de.philippst.alexa.weather.notification.util;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeoUtilsTest {

    @Test
    void polygonToWkt() {
        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] coordinates = {
                new Coordinate(11.721826,48.164603),
                new Coordinate(11.904254,48.068283),
                new Coordinate(11.717396,48.074631),
                new Coordinate(11.721826,48.164603)
        };

        assertEquals(
                "POLYGON ((11.721826 48.164603, 11.904254 48.068283, 11.717396 48.074631, 11.721826 48.164603))",
                GeoUtils.polygonToWkt(geometryFactory.createPolygon(coordinates))
        );
    }

    @Test
    void xmlStringToPolygon(){
        String xmlString = "48.874033,12.172564 48.896133,12.197176 48.896086,12.207871 48.919065,12.22756 48.874033,12.172564";
        Polygon polygon = GeoUtils.xmlStringToPolygon(xmlString);
        assertNotNull(polygon);
    }
}