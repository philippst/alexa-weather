package de.philippst.alexa.weather.notification;

import com.amazon.ask.model.services.proactiveEvents.CreateProactiveEventRequest;
import com.amazon.ask.model.services.proactiveEvents.SkillStage;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.notification.cap.*;
import de.philippst.alexa.weather.notification.exception.UnkownCapEventException;
import de.philippst.alexa.weather.notification.model.WeatherAlertType;
import de.philippst.alexa.weather.notification.service.AlexaAlertLogService;
import de.philippst.alexa.weather.notification.service.AlexaProactiveEventsService;
import de.philippst.alexa.weather.notification.service.AlexaUserService;
import de.philippst.alexa.weather.notification.util.WeatherAlertUtils;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AlertProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AlertProcessor.class);
    private AlexaUserService alexaUserService;
    private AlexaAlertLogService alexaAlertLogService;
    private AlexaProactiveEventsService alexaProactiveEventsService;

    public AlertProcessor(AlexaUserService alexaUserService, AlexaAlertLogService alexaAlertLogService) {
        this.alexaUserService = alexaUserService;
        this.alexaAlertLogService = alexaAlertLogService;
        this.alexaProactiveEventsService = new AlexaProactiveEventsService();
    }

    void processAlerts(List<Alert> alertList){
        alertList.forEach(this::processAlert);
    }

    /**
     * Handle incoming CAP alert
     * @param alert CAP alert pojo
     */
    private void processAlert(Alert alert){

        SkillStage skillStage = SkillStage.valueOf(System.getenv("SKILL_STAGE"));

        logger.info("Processing CAP alert: {}",alert);

        if(alert.getMsgType() != MsgType.ALERT){
            logger.debug("Ignore CAP alert because of msgType={}",alert.getMsgType());
            return;
        }

        if(skillStage == SkillStage.LIVE && alert.getStatus() != Status.ACTUAL){
            logger.warn("Dropped CAP Alert of status '{}' in skillStage '{}': {}",
                    alert.getStatus(),skillStage, alert.getIdentifier());
            return;
        }

        Info info = alert.getInfo();
/*
        this.alexaAlertLogService.save(new AlertLog(alert.getIdentifier(),alert.getSent(),
                alert.getInfo().getExpires()));
*/

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
        logger.info("Processing CAP alert '{}' for AlexaUser '{}'.",alert,alexaUser.getUserId());
        CreateProactiveEventRequest createProactiveEventRequest =
                WeatherAlertUtils.createProactiveEventRequest(alexaUser.getUserId(), alert.getIdentifier(), weatherAlertType);

        this.alexaProactiveEventsService.sendNotification(createProactiveEventRequest);
        this.alexaUserService.updateAlexaUserLastEvent(alexaUser.getUserId(),alert.getIdentifier());

        logger.debug("Sending notification to user was successful: {}",alexaUser.getUserId());
    }
}
