package de.philippst.alexa.weather.skill.service;

import com.amazon.ask.model.events.skillevents.Permission;
import de.philippst.alexa.weather.common.dwd.Severity;
import de.philippst.alexa.weather.common.mapper.AlexaUserRowMapper;
import de.philippst.alexa.weather.common.model.AlexaUser;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlexaUserService {

    private final Logger logger = LoggerFactory.getLogger(AlexaUserService.class);

    private Jdbi jdbi;

    @Inject
    public AlexaUserService(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void update(AlexaUser alexaUser) {

        Optional<AlexaUser> alexaUserDb = this.getById(alexaUser.getUserId());
        if(alexaUserDb.isPresent()) {
            logger.debug("Update user in DB:'{}'", alexaUser.getUserId());
            jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE alexa_user " +
                            "SET country_code = :countryCode, device_id = :deviceId, last_updated = :lastUpdated, " +
                            "location_name = :locationName, position = POINT(:lng,:lat), postal_code = :postalCode, " +
                            "permission_postalcode = 0b:permissionPostalcode, permission_notification = 0b:permissionNotification " +
                            ", subscription_weather_alert = 0b:subscriptionWeatherAlert , notification_level = " +
                            ":notificationLevel WHERE id = :userId")
                            .bindBean(alexaUser)
                            .execute()
            );
        } else {
            logger.debug("Insert user in DB:'{}'", alexaUser.getUserId());
            jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO alexa_user " +
                            "(id, country_code, device_id, last_updated, location_name, position, postal_code, permission_postalcode, permission_notification, subscription_weather_alert, notification_level) VALUES " +
                            "(:userId,:countryCode,:deviceId,:lastUpdated,:locationName,POINT(:lng,:lat),:postalCode,0b:permissionPostalcode,0b:permissionNotification,0b:subscriptionWeatherAlert,:notificationLevel)")
                            .bindBean(alexaUser)
                            .execute()
            );
        }
    }

    public Optional<AlexaUser> getById(String userId) {
        logger.debug("Lookup user in DB: '{}'",userId);

        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT *, ST_AsText(position) as latlng FROM alexa_user WHERE id = :id")
                        .bind("id",userId)
                        .map(new AlexaUserRowMapper())
                        .findFirst()
        );
    }

    public AlexaUser getOrCreateById(String userId) {
        logger.debug("Lookup user in DB: '{}'",userId);

        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT *, ST_AsText(position) as latlng FROM alexa_user WHERE id = :id")
                        .bind("id",userId)
                        .map(new AlexaUserRowMapper())
                        .findFirst()
        ).orElse(new AlexaUser(userId));
    }


    public void deleteById(String userId) {
        logger.debug("Delete user in db: '{}'",userId);
        jdbi.withHandle(handle ->
                handle.createUpdate("DELETE FROM alexa_user WHERE id = :id")
                        .bind("id",userId)
                        .execute()
        );
    }

    public void updateUserPermissions(String userId, List<Permission> acceptedPermissions){

        String permissionList = acceptedPermissions.stream()
                .map(Permission::getScope).collect(Collectors.joining(", "));
        logger.debug("Set UserPermissions '{}' for: '{}'",permissionList,userId);

        AlexaUser alexaUser = this.getById(userId)
                .orElseGet(() -> new AlexaUser(userId));

        alexaUser.setPermissionPostalcode(
                acceptedPermissions.stream().anyMatch(
                        perm -> perm.getScope().equals("alexa:devices:all:address:country_and_postal_code:read")));

        alexaUser.setPermissionNotification(
                acceptedPermissions.stream().anyMatch(
                        perm -> perm.getScope().equals("alexa::devices:all:notifications:write")));

        this.update(alexaUser);
    }

    public void updateWeatherAlertSubscription(String userId, boolean weatherAlertSubscription){

        logger.debug("Update subscriptionWeatherAlert to '{}' for: '{}'",weatherAlertSubscription,userId);

        AlexaUser alexaUser = this.getById(userId)
                .orElseGet(() -> new AlexaUser(userId));

        alexaUser.setSubscriptionWeatherAlert(weatherAlertSubscription);
        this.update(alexaUser);
    }

    public void updateUserNotificationHint(String userId){
        logger.debug("Update notification hint for user in DB:'{}'", userId);
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE alexa_user SET last_notification_hint = now() WHERE id = :userId")
                        .bind("userId",userId)
                        .execute()
        );
    }

    public void updateUserNotificationLevel(String userId, Severity severity){
        logger.debug("Update notification level to {} for user in DB:'{}'", severity.getPriority(),userId);
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE alexa_user SET notification_level = :level WHERE id = :userId")
                        .bind("userId",userId)
                        .bind("level",severity.getPriority())
                        .execute()
        );
    }


}