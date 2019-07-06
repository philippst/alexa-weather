package com.amazon.api.proactive;

import com.amazon.api.proactive.model.ProactiveEvent;
import com.amazon.api.proactive.model.schema.WeatherAlertType;
import de.philippst.alexa.weather.notification.util.WeatherAlertUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

class ProactiveNotificationServiceTest {

    @Test
    void sendNotification() throws IOException {

        ProactiveNotificationService notificationService = new ProactiveNotificationService();

        String userId = "amzn1.ask.account.XXXXXXXXXXX";
        String eventId = "TEST-6";
        WeatherAlertType weatherAlertType = WeatherAlertType.THUNDER_STORM;
        ProactiveEvent proactiveEvent = WeatherAlertUtils.createProactiveEvent(userId,eventId,weatherAlertType);
        proactiveEvent.setTimestamp(ZonedDateTime.now());
        proactiveEvent.setExpiryTime(ZonedDateTime.now().plusMinutes(10));

        notificationService.sendNotification(proactiveEvent);
    }


}