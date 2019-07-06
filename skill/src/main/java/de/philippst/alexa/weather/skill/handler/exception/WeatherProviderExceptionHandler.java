package de.philippst.alexa.weather.skill.handler.exception;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import de.philippst.alexa.weather.skill.exception.WeatherProvideException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class WeatherProviderExceptionHandler implements ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WeatherProviderExceptionHandler.class);

    @Override
    public boolean canHandle(HandlerInput input, Throwable throwable) {
        return throwable instanceof WeatherProvideException;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {
        logger.error("Catching WeatherProviderException", throwable);
        String speechText = "Es tut mir leid, aktuell kann ich keine Daten vom Deutschen Wetterdienst abrufen. " +
                "Bitte versuche es später nocheinmal.";
        return input.getResponseBuilder().withSpeech(speechText).build();
    }

}
