package de.philippst.alexa.weather.skill.handler.exception;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TechnicalExceptionHandler implements ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TechnicalExceptionHandler.class);

    @Override
    public boolean canHandle(HandlerInput input, Throwable throwable) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {
        logger.error("Unhandled exception: ",throwable);
        String speechText = "Es tut mir leid. Aktuell ist keine Abfrage der Wetterdaten möglich. " +
                "Bitte probiere es später noch einmal.";
        return input.getResponseBuilder().withSpeech(speechText).build();
    }
}