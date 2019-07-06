package de.philippst.alexa.weather.notification;

import com.amazon.api.proactive.ProactiveNotificationService;
import com.amazon.api.proactive.model.ProactiveEvent;
import com.amazon.api.proactive.model.schema.WeatherAlertType;
import de.philippst.alexa.weather.notification.cap.*;
import de.philippst.alexa.weather.notification.exception.UnkownCapEventException;
import de.philippst.alexa.weather.notification.model.AlertLog;
import de.philippst.alexa.weather.notification.model.AlexaEvent;
import de.philippst.alexa.weather.notification.model.AlexaUser;
import de.philippst.alexa.weather.notification.service.AlexaAlertLogService;
import de.philippst.alexa.weather.notification.service.AlexaUserService;
import de.philippst.alexa.weather.notification.util.WeatherAlertUtils;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlertProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AlertProcessor.class);
    private AlexaUserService alexaUserService;
    private AlexaAlertLogService alexaAlertLogService;
    private ProactiveNotificationService proactiveNotificationService = new ProactiveNotificationService();

    public AlertProcessor(AlexaUserService alexaUserService, AlexaAlertLogService alexaAlertLogService) {
        this.alexaUserService = alexaUserService;
        this.alexaAlertLogService = alexaAlertLogService;
    }

    public void processAlerts(List<Alert> alertList){
        alertList.forEach(this::processAlert);
    }

    /**
     * Handle incoming CAP alert
     * @param alert CAP alert pojo
     */
    public void processAlert(Alert alert){
        logger.info("Processing CAP alert: {}",alert);

        if(alert.getMsgType() != MsgType.ALERT){
            logger.debug("Ignore CAP alert because of msgType={}",alert.getMsgType());
            return;
        }

        Info info = alert.getInfo();

        this.alexaAlertLogService.save(new AlertLog(alert.getIdentifier(),alert.getSent(),alert.getInfo().getExpires()));

        try {
            WeatherAlertType weatherAlertType = WeatherAlertUtils.mapWeatherAlertType(info);

            List<AlexaUser> affectedUsers = new ArrayList<>();
            for(Area area : info.getArea()){
                if(area.getPolygon() == null || area.getPolygon().size() == 0) continue;
                for(Polygon polygon : area.getPolygon()){
                    affectedUsers.addAll(this.alexaUserService.findAlexaUserToNotify(polygon, info.getSeverity()));
                }
            }
            logger.debug("Total #{} AlexaUsers are affected by alert.",affectedUsers.size());

            for(AlexaUser affectedUser : affectedUsers){
                this.processAffectedUser(affectedUser, alert, weatherAlertType);
            }

        } catch (UnkownCapEventException e) {
            logger.warn("Unkown CAP Event:",e);
        }
    }

    private void processAffectedUser(AlexaUser alexaUser, Alert alert, WeatherAlertType weatherAlertType){
        logger.info("Processing CAP alert '{}' for AlexaUser ''.",alert,alexaUser.getId());
        ProactiveEvent proactiveEvent = WeatherAlertUtils.createProactiveEvent(alexaUser.getId(), alert.getIdentifier(), weatherAlertType);
        try {
            this.proactiveNotificationService.sendNotification(proactiveEvent);
            this.alexaAlertLogService.save(
                    new AlexaEvent(alert.getIdentifier(),alexaUser.getId(),proactiveEvent.getTimestamp(),proactiveEvent.getExpiryTime())
            );
            logger.debug("Sending notification to user was successful: {}",alexaUser.getId());
        } catch (IOException e) {
            logger.error("Sending Notification to user failed: {}",alexaUser.getId());
        }
    }
}
