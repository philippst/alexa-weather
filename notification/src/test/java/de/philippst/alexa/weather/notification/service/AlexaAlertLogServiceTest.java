package de.philippst.alexa.weather.notification.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;

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


}