package de.philippst.alexa.weather.notification.service;

import com.amazon.ask.model.services.proactiveEvents.SkillStage;
import de.philippst.alexa.weather.common.mapper.AlexaUserRowMapper;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.common.util.GeoUtils;
import de.philippst.alexa.weather.notification.cap.Severity;
import de.philippst.alexa.weather.notification.model.JtsPointMapper;
import org.jdbi.v3.core.Jdbi;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AlexaUserService {
    private final Logger logger = LoggerFactory.getLogger(AlexaUserService.class);

    private Jdbi jdbi;

    public AlexaUserService(Jdbi jdbi) {
        this.jdbi = jdbi;
        jdbi.registerColumnMapper(new JtsPointMapper());
    }

    public List<AlexaUser> findAlexaUserToNotify(Polygon polygon, Severity severity){

        boolean testMode = SkillStage.valueOf(System.getenv("SKILL_STAGE")) == SkillStage.DEVELOPMENT;

        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT *, ST_AsText(position) as latlng FROM alexa_user " +
                        "WHERE ST_CONTAINS(ST_GeomFromText(:alertpolygon), position) " +
                        "AND subscription_weather_alert = 1 AND notification_level <= :severityPriority " +
                        "AND (last_notification_timestamp < CURDATE() OR " +
                        "last_notification_timestamp is null) " +
                        "AND test = 0b:testMode")
                        .bind("alertpolygon", GeoUtils.polygonToWkt(polygon))
                        .bind("severityPriority", severity.getPriority())
                        .bind("testMode",testMode)
                        .map(new AlexaUserRowMapper())
                        .list()
        );
    }

    public void updateAlexaUserLastEvent(String userId, String eventId) {
        logger.debug("Update last user event for user:'{}'", userId);
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE alexa_user " +
                        "SET last_notification_timestamp = now(), last_notification_event = :eventId " +
                        "WHERE id = :userId")
                        .bind("eventId",eventId)
                        .bind("userId", userId)
                        .execute()
        );
    }
}
