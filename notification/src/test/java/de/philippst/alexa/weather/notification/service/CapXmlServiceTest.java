package de.philippst.alexa.weather.notification.service;

import com.amazonaws.util.IOUtils;
import de.philippst.alexa.weather.notification.cap.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CapXmlServiceTest {

    @Test
    void alertFromString() throws IOException {
        File file = new File("src/test/resources/cap/test.DEU.xml");
        String alertXmlString = IOUtils.toString(new FileInputStream(file));
        CapXmlService capXmlService = new CapXmlService();
        Alert alert = capXmlService.alertFromString(alertXmlString);

        assertEquals(
                "2.49.0.1.276.0.DWD.PVW.1547277960000.87e37802-10cd-4b17-8c28-eb5552dabac6.DEU",
                alert.getIdentifier(),
                "cap/alert/identifier"
        );

        assertEquals("CAP@dwd.de", alert.getSender(), "cap/alert/sender");

        LocalDateTime dateTime = LocalDateTime.of(2019,1,12,7,26,0,0);
        assertEquals(ZonedDateTime.ofStrict(dateTime,ZoneOffset.UTC,ZoneId.of("UTC")), alert.getSent(), "cap/alert/sent");

        assertEquals(Status.ACTUAL, alert.getStatus(), "cap/alert/status");
        assertEquals(MsgType.ALERT, alert.getMsgType(), "cap/alert/msgType");
        assertEquals("PVW",alert.getSource(), "cap/alert/source");
        assertEquals(Scope.PUBLIC,alert.getScope(), "cap/alert/scope");
        assertEquals("id:2.49.0.1.276.0.DWD.PVW.1547277960000.87e37802-10cd-4b17-8c28-eb5552dabac6",alert.getCode(), "cap/alert/code");

        Info info = alert.getInfo();
        assertNotNull(info,"cap/alert/info");
        assertEquals("de-DE", info.getLanguage(), "cap/alert/info/language");
        assertEquals(Category.MET, info.getCategory(), "cap/alert/info/category");
//        assertEquals("WINDBÖEN", info.getEvent(), "cap/alert/info/event");
        assertEquals(ResponseType.NONE, info.getResponseType(), "cap/alert/info/responseType");
        assertEquals(Urgency.IMMEDIATE, info.getUrgency(), "cap/alert/info/urgency");
        assertEquals(Severity.MINOR, info.getSeverity(), "cap/alert/info/severity");
        assertEquals(Certainty.LIKELY, info.getCertainty(), "cap/alert/info/certainty");

        LocalDateTime effectiveTime = LocalDateTime.of(2019,1,12,7,26,0,0);
        assertEquals(ZonedDateTime.ofStrict(effectiveTime,ZoneOffset.UTC,ZoneId.of("UTC")), info.getEffective(), "cap/alert/info/effective");

        LocalDateTime onsetTime = LocalDateTime.of(2019,1,12,14,0,0,0);
        assertEquals(ZonedDateTime.ofStrict(onsetTime,ZoneOffset.UTC,ZoneId.of("UTC")), info.getOnset(), "cap/alert/info/onset");

        LocalDateTime expiresTime = LocalDateTime.of(2019,1,13,9,0,0,0);
        assertEquals(ZonedDateTime.ofStrict(expiresTime,ZoneOffset.UTC,ZoneId.of("UTC")), info.getExpires(), "cap/alert/info/expires");

        assertEquals(224,info.getArea().size(),"cap/alert/info/area: total number");

        // First area object should contain a list of polygons, with one entry
        assertEquals(1,info.getArea().get(0).getPolygon().size(),"cap/alert/info/area/polygon: polygon available");

        // Polygon should include 182 coordinates
        assertEquals(182,info.getArea().get(0).getPolygon().get(0).getCoordinates().length,"cap/alert/info/area/polygon: entries of polygon");

        // Received event codes
        assertEquals(5,info.getEventCode().size(),"cap/alert/info/eventCode");

    }
}