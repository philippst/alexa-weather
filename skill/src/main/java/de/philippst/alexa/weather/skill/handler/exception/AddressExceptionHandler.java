package de.philippst.alexa.weather.skill.handler.exception;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import de.philippst.alexa.weather.skill.exception.AddressException;

import java.util.Optional;

public class AddressExceptionHandler implements ExceptionHandler {

    @Override
    public boolean canHandle(HandlerInput input, Throwable throwable) {
        return throwable instanceof AddressException;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {
        String speechText = "Es tut mir leid. Ich konnte deine Postleitzahl nicht eindeutig einem " +
                "Wetterwarngebiet in Deutschland zuordnen. Bitte prüfe in der Alexa App ob deine Postleitzahl korrekt" +
                " erfasst wurde.";
        return input.getResponseBuilder().withSpeech(speechText).build();
    }

}
