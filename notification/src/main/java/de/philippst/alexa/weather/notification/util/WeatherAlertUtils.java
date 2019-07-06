package de.philippst.alexa.weather.notification.util;

import com.amazon.api.proactive.model.AudiencePayload;
import com.amazon.api.proactive.model.AudienceType;
import com.amazon.api.proactive.model.ProactiveEvent;
import com.amazon.api.proactive.model.RelevantAudience;
import com.amazon.api.proactive.model.schema.EventSchema;
import com.amazon.api.proactive.model.schema.LocalizedAttribute;
import com.amazon.api.proactive.model.schema.WeatherAlertActivatedSchema;
import com.amazon.api.proactive.model.schema.WeatherAlertType;
import de.philippst.alexa.weather.notification.cap.EventCode;
import de.philippst.alexa.weather.notification.cap.Info;
import de.philippst.alexa.weather.notification.exception.UnkownCapEventException;

import java.time.ZonedDateTime;
import java.util.Arrays;
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

    public static ProactiveEvent createProactiveEvent(String alexaUserId, String alertId, WeatherAlertType weatherAlertType){

        RelevantAudience relevantAudience = new RelevantAudience();
        relevantAudience.setType(AudienceType.Unicast);
        relevantAudience.setPayload(new AudiencePayload(alexaUserId));

        EventSchema eventSchema = new WeatherAlertActivatedSchema(weatherAlertType,"localizedattribute:source");

        ProactiveEvent proactiveEvent = new ProactiveEvent();
        proactiveEvent.setTimestamp(ZonedDateTime.now());
        proactiveEvent.setReferenceId(alertId);
        proactiveEvent.setExpiryTime(ZonedDateTime.now().plusMinutes(30));
        proactiveEvent.setEvent(eventSchema);
        proactiveEvent.setRelevantAudience(relevantAudience);

        LocalizedAttribute localizedAttribute = new LocalizedAttribute();
        localizedAttribute.setLocale("de-DE");
        localizedAttribute.setSource("Wetterwarnung");

        localizedAttribute.setLocale("en-US");
        localizedAttribute.setSource("Wetterwarnung");

        proactiveEvent.setLocalizedAttributes(Arrays.asList(localizedAttribute));

        return null;
    }

}
