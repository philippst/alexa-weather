package de.philippst.alexa.weather.notification.util;

import de.philippst.alexa.weather.notification.model.RelevantAudiencePayload;
import de.philippst.alexa.weather.notification.model.WeatherAlertType;
import com.amazon.ask.model.services.proactiveEvents.CreateProactiveEventRequest;
import com.amazon.ask.model.services.proactiveEvents.Event;
import com.amazon.ask.model.services.proactiveEvents.RelevantAudience;
import com.amazon.ask.model.services.proactiveEvents.RelevantAudienceType;
import de.philippst.alexa.weather.notification.cap.EventCode;
import de.philippst.alexa.weather.notification.cap.Info;
import de.philippst.alexa.weather.notification.exception.UnkownCapEventException;
import de.philippst.alexa.weather.notification.model.EventWeatherAlertPayload;

import java.time.OffsetDateTime;
import java.util.List;

public class WeatherAlertUtils {

    public static WeatherAlertType mapWeatherAlertType(Info info) throws UnkownCapEventException {
        List<EventCode> eventCodeList = info.getEventCode();

        for(EventCode code : eventCodeList){
            if (code.getValueName().equals("GROUP")) return WeatherAlertUtils.capEventCode(code.getValue());
        }
        throw new UnkownCapEventException("No valid cap event code found.");
    }

    public static WeatherAlertType capEventCode(String capCode) throws UnkownCapEventException {
        switch (capCode){
            case "THUNDERSTORM":
            case "HAIL":
            case "RAIN":
            case "WIND":
                return WeatherAlertType.THUNDER_STORM;
            case "TORNADO":
                return WeatherAlertType.TORNADO;
            case "SNOWFALL":
            case "SNOWDRIFT":
                return WeatherAlertType.SNOW_STORM;
            case "FOG":
            case "FROST":
            case "GLAZE":
            case "THAW":
            case "POWERLINEVIBRATION":
            case "UV":
            case "HEAT":
            case "TEST":
            default:
                throw new UnkownCapEventException("CapCode is unkown: "+capCode);
        }
    }

    public static CreateProactiveEventRequest createProactiveEventRequest(String alexaUserId, String alertId,
                                                                          WeatherAlertType weatherAlertType){

        RelevantAudiencePayload relevantAudiencePayload = RelevantAudiencePayload.builder().withUser(alexaUserId).build();

        RelevantAudience relevantAudience = RelevantAudience.builder()
                .withPayload(relevantAudiencePayload)
                .withType(RelevantAudienceType.UNICAST)
                .build();

        EventWeatherAlertPayload eventWeatherAlertPayload = new EventWeatherAlertPayload(weatherAlertType,
                "localizedattribute:source");

        Event event = Event.builder()
                    .withName("AMAZON.WeatherAlert.Activated")
                    .withPayload(eventWeatherAlertPayload)
                    .build();

        CreateProactiveEventRequest createProactiveEventRequest = CreateProactiveEventRequest.builder()
                .withEvent(event)
                .withTimestamp(OffsetDateTime.now())
                .withReferenceId(alertId)
                .withExpiryTime(OffsetDateTime.now().plusMinutes(30))
                .withRelevantAudience(relevantAudience)
                .build();

        return createProactiveEventRequest;
    }
}
