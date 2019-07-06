package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.ProactiveSubscriptionChangedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.events.skillevents.ProactiveSubscriptionChangedRequest;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class ProactiveSubscriptionChangedHandler implements ProactiveSubscriptionChangedRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(ProactiveSubscriptionChangedHandler.class);
    private AlexaUserService alexaUserService;

    @Inject
    public ProactiveSubscriptionChangedHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input, ProactiveSubscriptionChangedRequest proactiveSubscriptionChangedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input,
                                     ProactiveSubscriptionChangedRequest proactiveSubscriptionChangedRequest) {

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("Skill proactive subscription changed without userId."));

        logger.debug("Calling ProactiveSubscriptionChangedHandler with userId: '{}'", userId);

        if(proactiveSubscriptionChangedRequest.getBody() == null){
            this.alexaUserService.updateWeatherAlertSubscription(userId, false);
            return Optional.empty();
        }

        boolean weatherAlertSubscription = proactiveSubscriptionChangedRequest.getBody().getSubscriptions().stream().anyMatch(
                s -> s.getEventName().equals("AMAZON.WeatherAlert.Activated")
        );
        this.alexaUserService.updateWeatherAlertSubscription(userId, weatherAlertSubscription);

        return Optional.empty();
    }
}
