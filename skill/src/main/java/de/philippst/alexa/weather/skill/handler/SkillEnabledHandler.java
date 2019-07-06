package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.SkillEnabledRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.events.skillevents.SkillEnabledRequest;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class SkillEnabledHandler implements SkillEnabledRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(SkillEnabledHandler.class);

    private AlexaUserService alexaUserService;

    @Inject
    public SkillEnabledHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input, SkillEnabledRequest skillEnabledRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, SkillEnabledRequest skillEnabledRequest) {

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("Skill enabled without userId."));

        logger.debug("Calling SkillEnabledHandler with userId: '{}'", userId);

        AlexaUser alexaUser = new AlexaUser(userId);
        this.alexaUserService.update(alexaUser);
        return Optional.empty();
    }
}
