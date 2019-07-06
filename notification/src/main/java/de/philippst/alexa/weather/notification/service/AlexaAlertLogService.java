package de.philippst.alexa.weather.notification.service;

import de.philippst.alexa.weather.notification.model.AlertLog;
import de.philippst.alexa.weather.notification.model.AlexaEvent;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AlexaAlertLogService {
    private final Logger logger = LoggerFactory.getLogger(AlexaUserService.class);

    private Jdbi jdbi;

    public AlexaAlertLogService(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void save(AlexaEvent alexaEvent) {
        Optional<AlexaEvent> alexaEventDb = this.getEventById(alexaEvent.getReferenceId(),alexaEvent.getUserId());

        if(alexaEventDb.isPresent()){
            jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE alexa_event " +
                            "SET created = :created, expires = :expires " +
                            "WHERE reference_id = :referenceId AND user_id = :userId")
                            .bindBean(alexaEvent)
                            .execute()
            );
        } else {
            jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO alexa_event (reference_id, user_id, created, expires) " +
                            "VALUES (:referenceId,:userId,:created,:expires)")
                            .bindBean(alexaEvent)
                            .execute()
            );
        }
    }

    public Optional<AlexaEvent> getEventById(String referenceId, String userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM alexa_event " +
                        "WHERE reference_id = :referenceId AND user_id = :userId")
                        .bind("referenceId",referenceId)
                        .bind("userId",userId)
                        .mapToBean(AlexaEvent.class)
                        .findFirst()
        );
    }

    public void save(AlertLog alertLog) {

        Optional<AlertLog> alertLogDb = this.getAlertById(alertLog.getId());

        if(alertLogDb.isPresent()){
            jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE alert_log SET `timestamp` = :timestamp, expires = :expires WHERE id = :id")
                            .bindBean(alertLog)
                            .execute()
            );
        } else {
            jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO alert_log (id, `timestamp`, expires) VALUES (:id,:timestamp,:expires)")
                            .bindBean(alertLog)
                            .execute()
            );
        }
    }

    public Optional<AlertLog> getAlertById(String alertId) {
        return jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM alert_log WHERE id = :id")
                        .bind("id",alertId)
                        .mapToBean(AlertLog.class)
                        .findFirst()
        );
    }

}
