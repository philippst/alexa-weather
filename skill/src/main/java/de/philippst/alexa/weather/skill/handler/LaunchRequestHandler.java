package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.common.dwd.Alert;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.common.model.Position;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import de.philippst.alexa.weather.skill.service.WeatherAlertService;
import de.philippst.alexa.weather.skill.util.AlexaSSml;
import de.philippst.alexa.weather.skill.util.CustomRequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(LaunchRequestHandler.class);

    private final WeatherAlertService weatherAlertService;
    private final AlexaUserService alexaUserService;

    @Inject
    public LaunchRequestHandler(WeatherAlertService weatherAlertService, AlexaUserService alexaUserService) {
        this.weatherAlertService = weatherAlertService;
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class).or(intentName("WeatherAlert")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        logger.debug("Calling LaunchRequestHandler with userId: '{}' and deviceId: '{}'",
                RequestHelper.forHandlerInput(input).getUserId().orElse("NONE"),
                CustomRequestHelper.forHandlerInput(input).getDeviceId().orElse("NONE"));

        AlexaUser alexaUser = (AlexaUser) input.getAttributesManager().getRequestAttributes().get("AlexaUser");
        logger.debug("Launch Request Intent for AlexaUser: {} {}",alexaUser.getLat(),alexaUser.getLng());

        Position position = new Position(alexaUser.getLat(),alexaUser.getLng());
        position.setLocationName(alexaUser.getLocationName());

        List<Alert> alerts = this.weatherAlertService.getAlertsByPosition(position);

        if (alerts.size() > 0) {
            Collections.sort(alerts);
            alerts = this.weatherAlertService.removeDuplicates(alerts);
            return this.getAlertsResponse(input, alerts, position);
        } else {
            return this.getNoAlertsResponse(input, alexaUser);
        }
    }

    private Optional<Response> getNoAlertsResponse(HandlerInput input, AlexaUser alexaUser) {
        String speechText = "Es liegen aktuell keine Wetterwarnungen vor.";

        if(!alexaUser.getSubscriptionWeatherAlert() && (alexaUser.getLastNotificationHint() == null
                || Period.between(alexaUser.getLastNotificationHint(),LocalDate.now()).getDays() > 30)){

            speechText = speechText + " Übrigens; im Falle eines nahenden Unwetters kann ich dich auch " +
                    "vorwarnen. Öffne jetzt die Alexa App und aktiviere die Wetterwarnung Benachrichtigung.";
            this.alexaUserService.updateUserNotificationHint(alexaUser.getUserId());

            String permissionConsent = "alexa::devices:all:notifications:write";

            return input.getResponseBuilder()
                    .withAskForPermissionsConsentCard(Collections.singletonList(permissionConsent))
                    .withSpeech(speechText).build();

        } else {
            return input.getResponseBuilder().withSpeech(speechText).build();
        }

    }

    private Optional<Response> getAlertsResponse(HandlerInput input, List<Alert> alerts, Position position) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Alert> speechAlerts = alerts.stream().limit(2).collect(Collectors.toList());

        for (Alert alert : speechAlerts) {

            LocalDateTime onset = alert.getOnset().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime expires = alert.getExpires().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            String alertHeadline = alert.getHeadline().replaceAll("\\(.+?\\)\\s", "");

            stringBuilder.append(alertHeadline);
            stringBuilder.append("<break strength='medium'/>");

            stringBuilder.append("Gültig ");

            if (onset.isAfter(LocalDateTime.now())) {
                stringBuilder.append(
                        String.format(
                                "von %s <say-as interpret-as='time'>%s</say-as> ",
                                AlexaSSml.dateToSpeech(onset.toLocalDate(), LocalDate.now()),
                                onset.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        )
                );
            }

            String expiresText = String.format(
                    "bis %s <say-as interpret-as='time'>%s</say-as>. ",
                    AlexaSSml.dateToSpeech(expires.toLocalDate(), LocalDate.now()),
                    expires.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            );
            stringBuilder.append(expiresText);

            String alertDescription = alert.getDescription();
            alertDescription = alertDescription
                    .replaceAll("\\(.+?\\)\\s", "")
                    .replaceAll("SPF","Lichtschutzfaktor");
            stringBuilder.append(alertDescription);

            String alertInstruction = alert.getInstruction();
            if (alertInstruction != null) {
                alertInstruction = alertInstruction
                        .replaceAll("ACHTUNG! Hinweis auf mögliche Gefahren:",
                                "<say-as interpret-as=\"interjection\">achtung!</say-as>");
            }
            if (alertInstruction != null) stringBuilder.append("<break strength='strong'/>" + alertInstruction);
            stringBuilder.append("<break strength='x-strong'/>");
        }

        if (alerts.size() > 2) {
            String additionalAlerts = "" +
                    "<break time='1s'/> Achtung, es liegen Zusatzwarnungen vor. " +
                    "Du findest diese jetzt in der Alexa App.";
            stringBuilder.append(additionalAlerts);
        }

        String outputText = "<speak>" + stringBuilder.toString() + "</speak>";
        logger.info("Output Speech: {}/{} | {}", position.getLat(), position.getLng(), outputText);

        return input.getResponseBuilder()
                .withSpeech(outputText)
                .withSimpleCard("Aktuelle Warnlage", this.getAlertsCardContent(alerts))
                .build();
    }

    private String getAlertsCardContent(List<Alert> alerts) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Alert alert : alerts) {
            LocalDateTime expires = alert.getExpires().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime onset = alert.getOnset().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            stringBuilder.append(alert.getHeadline()).append("\n");
            stringBuilder.append(
                    String.format(
                            "Gültig von %s bis %s",
                            onset.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                            expires.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                    )
            ).append("\n\n");
            stringBuilder.append(alert.getDescription()).append("\n");
        }

        String cardContent = stringBuilder.toString();
        logger.info("Output Card Content: {}", cardContent);

        return cardContent;

    }
}
