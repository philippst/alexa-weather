package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import javax.inject.Inject;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

    @Inject
    public HelpIntentHandler() {
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        String helpText = "" +
                "Dieser Skill informiert über aktuelle Wetterwarnungen. Sage einfach 'Alexa, starte Wetterwarnung' . " +
                "Um zu prüfen, ob du bei drohenden Unwetter gewarnt wirst, sage 'Alexa, frage Wetterwarnung ob ich " +
                "Benachrichtigungen erhalte' .";

        return input.getResponseBuilder()
                .withSpeech(helpText)
                .build();
    }

}
