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
import java.util.Optional;

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
                        "AND subscription_weather_alert = 1 AND notification_level >= :severityPriority " +
                        "AND test = 0b:testMode")
                        .bind("alertpolygon", GeoUtils.polygonToWkt(polygon))
                        .bind("severityPriority", severity.getPriority())
                        .bind("testMode",testMode)
                        .map(new AlexaUserRowMapper())
                        .list()
        );
    }

    public Optional<AlexaUser> getById(String userId) {
        logger.debug("Lookup user in database: '{}'",userId);

        return jdbi.withHandle(handle ->
                        handle.createQuery("SELECT id, country_code, device_id, last_updated, " +
                                "location_name, notification, postal_code, ST_AsText(position) as position " +
                                "FROM alexa_user WHERE id = :id")
                        .bind("id",userId)
                        .map(new AlexaUserRowMapper())
                        .findFirst()
        );
    }

}
