package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NotificationStatusHandler implements RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStatusHandler.class);

    private AlexaUserService alexaUserService;

    @Inject
    public NotificationStatusHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("NotificationStatus"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("NotificationStatusHandler requested without userId."));

        AlexaUser alexaUser = this.alexaUserService.getById(userId).orElseThrow(() -> new TechnicalException("User not found"));

        if(!alexaUser.getSubscriptionWeatherAlert()) return this.notificationDisabledResponse(input,alexaUser);
        return notificationEnabledResponse(input,alexaUser);
    }

    private Optional<Response> notificationDisabledResponse(HandlerInput input, AlexaUser alexaUser){
        String helpText = "Du erhältst keine Benachrichtigungen bei Wetterwarnungen. " +
                "Öffne jetzt die Alexa App und aktiviere die Benachrichtigung.";

        return input.getResponseBuilder()
                .withSpeech(helpText)
                .withAskForPermissionsConsentCard(Collections.singletonList("alexa::devices:all:notifications:write"))
                .build();
    }

    private Optional<Response> notificationEnabledResponse(HandlerInput input, AlexaUser alexaUser){
        String helpText = String.format(
                "Du erhältst Benachrichtigungen ab Stufe %s; '%s' '%s'. " +
                "Sage 'Alexa, ändere Benachrichtigung von Wetterwarnung.' um dies zu ändern.",
                alexaUser.getSeverity().getLevel(),
                alexaUser.getSeverity().getDescription(),
                alexaUser.getSeverity().getColorGerman()
        );

        return input.getResponseBuilder()
                .withSpeech(helpText)
                .build();
    }


}
