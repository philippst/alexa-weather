package de.philippst.alexa.weather.skill.handler.exception;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import de.philippst.alexa.weather.skill.exception.PermissionMissingException;

import java.util.Collections;
import java.util.Optional;

public class PermissionMissingExceptionHandler implements ExceptionHandler {
    @Override
    public boolean canHandle(HandlerInput input, Throwable throwable) {
        return throwable instanceof PermissionMissingException;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {
        String speechText = "" +
                "Um Wetterwarnungen in deiner Region ermitteln zu können, benötige ich Zugriff auf deine Postleitzahl." +
                " Du kannst jetzt in der Alexa App den Zugriff auf deine Postleitzahl freigeben." +
                " Der Skill 'Wetterwarnung' funktioniert nicht ohne Zugriff auf deine Postleitzahl.";

        String permissionPostalcode = "read::alexa:device:all:address:country_and_postal_code";

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withAskForPermissionsConsentCard(Collections.singletonList(permissionPostalcode))
                .build();
    }

}
