package de.philippst.alexa.weather.skill.mapper;

import com.github.filosganga.geogson.model.Point;
import de.philippst.alexa.weather.common.dwd.Severity;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.skill.util.GeoUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AlexaUserRowMapper implements RowMapper<AlexaUser> {

    @Override
    public AlexaUser map(ResultSet rs, StatementContext ctx) throws SQLException {
        Point position = GeoUtils.wktToPoint(rs.getString("latlng"));
        AlexaUser alexaUser = new AlexaUser(rs.getString("id"));
        alexaUser.setCountryCode(rs.getString("country_code"));
        alexaUser.setPostalCode(rs.getString("postal_code"));
        alexaUser.setDeviceId(rs.getString("device_id"));
        if(position != null){
            alexaUser.setLng(position.lon());
            alexaUser.setLat(position.lat());
        }
        alexaUser.setLocationName(rs.getString("location_name"));
        alexaUser.setPermissionPostalcode(rs.getBoolean("permission_postalcode"));
        alexaUser.setPermissionNotification(rs.getBoolean("permission_notification"));
        alexaUser.setSubscriptionWeatherAlert(rs.getBoolean("subscription_weather_alert"));
        alexaUser.setNotificationLevel(rs.getInt("notification_level"));
        alexaUser.setSeverity(Severity.fromPriority(rs.getInt("notification_level")));

        if(rs.getDate("last_notification_hint") != null){
            alexaUser.setLastNotificationHint(rs.getDate("last_notification_hint").toLocalDate());
        }
        return alexaUser;
    }
}
