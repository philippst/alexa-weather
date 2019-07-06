package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.PermissionChangedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.events.skillevents.PermissionChangedRequest;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class PermissionChangedHandler implements PermissionChangedRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(PermissionChangedHandler.class);

    private AlexaUserService alexaUserService;

    @Inject
    public PermissionChangedHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input, PermissionChangedRequest permissionChangedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, PermissionChangedRequest permissionChangedRequest) {

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("Skill permission changed without userId."));

        logger.debug("Calling PermissionChangedHandler with userId: '{}'",userId);

        this.alexaUserService.updateUserPermissions(userId,permissionChangedRequest.getBody().getAcceptedPermissions());

        return Optional.empty();
    }





}
