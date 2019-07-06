package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.SkillDisabledRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.events.skillevents.SkillDisabledRequest;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class SkillDisabledHandler implements SkillDisabledRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(SkillDisabledHandler.class);

    private AlexaUserService alexaUserService;

    @Inject
    public SkillDisabledHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input, SkillDisabledRequest skillDisabledRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, SkillDisabledRequest skillDisabledRequest) {

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("Skill disabled without userId."));

        logger.debug("Calling SkillDisabledHandler with userId: '{}'", userId);

        this.alexaUserService.deleteById(userId);
        return Optional.empty();
    }
}
