package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.PermissionAcceptedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.events.skillevents.PermissionAcceptedRequest;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class PermissionAcceptedHandler implements PermissionAcceptedRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(PermissionAcceptedHandler.class);

    private AlexaUserService alexaUserService;

    @Inject
    public PermissionAcceptedHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input, PermissionAcceptedRequest permissionAcceptedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, PermissionAcceptedRequest permissionAcceptedRequest) {

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("Skill permission accepted without userId."));

        logger.debug("Calling PermissionAcceptedHandler with userId: '{}'",userId);

        this.alexaUserService.updateUserPermissions(userId,permissionAcceptedRequest.getBody().getAcceptedPermissions());

        return Optional.empty();
    }
}
