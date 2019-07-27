package de.philippst.alexa.weather.notification.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.philippst.alexa.weather.notification.cap.*;
import de.philippst.alexa.weather.notification.model.AlertLog;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

class AlexaAlertLogServiceTest {

    @Test
    void getById() {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/alexa_weather?useSSL=false");
        dataSource.setUser("root");
        dataSource.setPassword("XXXXXXXXXXXX");

        Jdbi jdbi = Jdbi.create(dataSource);
        AlexaAlertLogService alexaAlertLogService = new AlexaAlertLogService(jdbi);
//        Optional<AlertLog> alertLogOptional = alexaAlertLogService.getAlertById("TEST-9999");
//        System.out.println(alertLogOptional.get().getId()+" "+alertLogOptional.get().getTimestamp());

//        AlertLog alertLog = new AlertLog("TEST12345", ZonedDateTime.now(),ZonedDateTime.now());
//        AlexaEvent alexaEvent = new AlexaEvent(alertLog.getId(),"9998-Munich",ZonedDateTime.now(),ZonedDateTime.now());
//        alexaAlertLogService.save(alertLog);
//        alexaAlertLogService.save(alexaEvent);
    }


    @Test
    void insert() {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/alexa_weather?useSSL=false");
        dataSource.setUser("root");
        dataSource.setPassword("XXXXXXXXXXXX");

        Jdbi jdbi = Jdbi.create(dataSource);
        AlexaAlertLogService alexaAlertLogService = new AlexaAlertLogService(jdbi);

        AlertLog alertLog = new AlertLog();
        alertLog.setId("99999999999999999999999999999999");
        alertLog.setCreatedTimestamp(LocalDateTime.now());
        alertLog.setUpdatedTimestamp(LocalDateTime.now());
        alertLog.setMsgCode("msg code");
        alertLog.setSender("DWD");
        alertLog.setSent(ZonedDateTime.now());
        alertLog.setMsgScope(Scope.PUBLIC);
        alertLog.setMsgType(MsgType.ALERT);
        alertLog.setMsgSource("source");
        alertLog.setMsgStatus(Status.TEST);
        alertLog.setSeverity(Severity.UNKOWN);
        alertLog.setUrgency(Urgency.EXPECTED);
        alertLog.setCertainty(Certainty.LIKELY);
        alertLog.setCategory(Category.ENV);
        alertLog.setHeadline("Test Headline");
        alertLog.setEvent("Event Description");
        alexaAlertLogService.save(alertLog);
    }
}