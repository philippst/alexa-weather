package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Resolutions;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.common.dwd.Severity;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NotificationChangeHandler implements RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationChangeHandler.class);

    private AlexaUserService alexaUserService;

    @Inject
    public NotificationChangeHandler(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("NotificationChange"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String userId = RequestHelper.forHandlerInput(input).getUserId().orElseThrow(()-> new TechnicalException(
                "Missing user id"));

        Slot severitySlot = RequestHelper.forHandlerInput(input).getSlot("AlertSeverity")
                .orElseThrow(() -> new TechnicalException("Missing AlertSeverity"));

        String slotId = this.getSlotId(severitySlot).orElseThrow(() -> new TechnicalException("Slot resolution " +
                "failed"));

        Severity severity = Severity.valueOf(slotId);

        logger.debug("Change notification level to {}/{} for userId='{}'",severity,severity.getPriority(),userId);

        this.alexaUserService.updateUserNotificationLevel(userId,severity);

        String helpText = String.format("Ok, du erhältst nun Benachrichtigungen ab Stufe %s; '%s' '%s'.",
                severity.getLevel(),
                severity.getDescription(),
                severity.getColorGerman()
        );

        return input.getResponseBuilder()
                .withSpeech(helpText)
                .build();

    }

    private Optional<String> getSlotId(Slot slot) {
        Resolutions resolutions = slot.getResolutions();
        if (resolutions != null) {
            return resolutions.getResolutionsPerAuthority().stream()
                    .filter(r -> r.getStatus().getCode() == StatusCode.ER_SUCCESS_MATCH)
                    .map(r -> r.getValues().get(0).getValue().getId())
                    .findAny();
        }
        return Optional.empty();
    }

}
