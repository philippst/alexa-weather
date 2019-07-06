package de.philippst.alexa.weather.skill.handler;


import com.amazon.api.proactive.model.AudiencePayload;
import com.amazon.api.proactive.model.AudienceType;
import com.amazon.api.proactive.model.ProactiveEvent;
import com.amazon.api.proactive.model.RelevantAudience;
import com.amazon.api.proactive.model.schema.EventSchema;
import com.amazon.api.proactive.model.schema.WeatherAlertActivatedSchema;
import com.amazon.api.proactive.model.schema.WeatherAlertType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AlexaEventSchemaTest {

    @BeforeAll
    static void initAll() {
    }

    @Test
    void myFirstTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.registerModule(new JavaTimeModule());

        RelevantAudience relevantAudience = new RelevantAudience();
        relevantAudience.setType(AudienceType.Unicast);
        relevantAudience.setPayload(new AudiencePayload("userId"));

        EventSchema eventSchema = new WeatherAlertActivatedSchema(WeatherAlertType.TORNADO,"Wetterwarnung");

        ProactiveEvent proactiveEvent = new ProactiveEvent();
        proactiveEvent.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        proactiveEvent.setReferenceId("");
        proactiveEvent.setExpiryTime(ZonedDateTime.now(ZoneOffset.UTC).plusHours(5));
        proactiveEvent.setEvent(eventSchema);
        proactiveEvent.setRelevantAudience(relevantAudience);

        String jsonResult = objectMapper.writeValueAsString(proactiveEvent);

        System.out.println(jsonResult);
    }


}