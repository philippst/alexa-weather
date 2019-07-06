package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import javax.inject.Inject;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ExitIntentHandler implements RequestHandler {

    @Inject
    public ExitIntentHandler() {
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.StopIntent").or(intentName("AMAZON.CancelIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder().build();
    }

}
