package de.philippst.alexa.weather.notification.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.philippst.alexa.weather.notification.util.JtsPolygonDeserializer;
import de.philippst.alexa.weather.notification.cap.Alert;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple service for parsing CAP XML strings to CAP POJOs via Jackson XMLMapper.
 * Class should be handled as singleton to reuse XmlMapper instance.
 */
public class CapXmlService {

    private static final Logger logger = LoggerFactory.getLogger(CapXmlService.class);

    private XmlMapper xmlMapper;

    public CapXmlService(){
        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        SimpleModule jtsModule = new SimpleModule();
        jtsModule.addDeserializer(Polygon.class, new JtsPolygonDeserializer());
        xmlMapper.registerModule(jtsModule);
    }

    public List<Alert> alertsFromString(List<String> stringList) throws IOException {
        List<Alert> alerts = new ArrayList<>();
        for(String alert : stringList){
            alerts.add(this.alertFromString(alert));
        }
        return alerts;
    }

    public Alert alertFromString(String alertString) throws IOException {
        logger.debug("Converting CAP XML to AlertLog Object: {}...",alertString.substring(0,20));
        Alert alert = xmlMapper.readValue(alertString,Alert.class);
        logger.debug("Converted to CAP AlertLog: {}",alert.getIdentifier());
        return alert;
    }
}
