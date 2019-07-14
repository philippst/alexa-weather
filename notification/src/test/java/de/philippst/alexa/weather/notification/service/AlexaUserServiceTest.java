package de.philippst.alexa.weather.notification.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.notification.cap.Severity;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlexaUserServiceTest {

    @Test
    void findAlexaUserToNotify() {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/alexa_weather?useSSL=false");
        dataSource.setUser("root");
        dataSource.setPassword("XXXXXXXXXXXX");

        Jdbi jdbi = Jdbi.create(dataSource);
        AlexaUserService userService = new AlexaUserService(jdbi);

        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] coordinates = {
                new Coordinate(11.721826,48.164603),
                new Coordinate(11.884559,48.160821),
                new Coordinate(11.904254,48.068283),
                new Coordinate(11.717396,48.074631),
                new Coordinate(11.721826,48.164603)
        };
        Polygon polygon = geometryFactory.createPolygon(coordinates);

        Severity severity = Severity.EXTREME;

        List<AlexaUser> userList = userService.findAlexaUserToNotify(polygon,severity);
        assertEquals(2,userList.size());
    }
}