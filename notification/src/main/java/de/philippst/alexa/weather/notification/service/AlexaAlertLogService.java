package de.philippst.alexa.weather.notification.service;

import de.philippst.alexa.weather.notification.cap.Alert;
import de.philippst.alexa.weather.notification.cap.Info;
import de.philippst.alexa.weather.notification.model.AlertLog;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

public class AlexaAlertLogService {
    private final Logger logger = LoggerFactory.getLogger(AlexaAlertLogService.class);

    private Jdbi jdbi;

    public AlexaAlertLogService(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void save(Alert alert) {

        Info info = alert.getInfo();

        AlertLog alertLog = new AlertLog();
        alertLog.setId(alert.getIdentifier());
        alertLog.setCreatedTimestamp(LocalDateTime.now());
        alertLog.setSender(alert.getSender());
        alertLog.setSent(alert.getSent());
        alertLog.setMsgStatus(alert.getStatus());
        alertLog.setMsgType(alert.getMsgType());
        alertLog.setMsgCode(alert.getCode());
        alertLog.setMsgScope(alert.getScope());
        alertLog.setMsgSource(alert.getSource());
        alertLog.setSeverity(info.getSeverity());
        alertLog.setUrgency(info.getUrgency());
        alertLog.setCertainty(info.getCertainty());
        alertLog.setCategory(info.getCategory());
        alertLog.setHeadline(info.getHeadline());
        alertLog.setEvent(info.getEvent());
        this.save(alertLog);
    }

    public void save(AlertLog alertLog) {

        Optional<AlertLog> alertLogDb = this.getAlertById(alertLog.getId());

        if(alertLogDb.isPresent()){
            jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE alert_log SET `updated_timestamp` = :updatedTimestamp, " +
                            "sender = :sender, sent = :sent, msg_status = :msgStatus, msg_type = :msgType, " +
                            "msg_source = :msgSource, msg_scope = :msgScope, msg_code = :msgCode, " +
                            "severity = :severity, urgency = :urgency, certainty = :certainty, category = :category, " +
                            "headline = :headline, event = :event " +
                            "WHERE id = :id")
                            .bindBean(alertLog)
                            .execute()
            );
        } else {
            jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO alert_log (id, created_timestamp, sender, sent, msg_status, " +
                            "msg_type,msg_source,msg_scope,msg_code,severity,urgency,certainty,category,headline," +
                            "event) " +
                            "VALUES (:id,:createdTimestamp,:sender,:sent,:msgStatus,:msgType,:msgSource,:msgScope," +
                            ":msgCode,:severity,:urgency,:certainty,:category,:headline,:event)")
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
