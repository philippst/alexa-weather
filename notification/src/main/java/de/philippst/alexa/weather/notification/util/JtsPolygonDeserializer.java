package de.philippst.alexa.weather.notification.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;

public class JtsPolygonDeserializer extends StdDeserializer<Polygon> {

    public JtsPolygonDeserializer() {
        this(null);
    }

    public JtsPolygonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Polygon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return GeoUtils.xmlStringToPolygon(p.getValueAsString());
    }
}
