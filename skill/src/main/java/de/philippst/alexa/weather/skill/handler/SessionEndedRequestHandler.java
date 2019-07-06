package de.philippst.alexa.weather.skill.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class SessionEndedRequestHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(SessionEndedRequestHandler.class);


    @Inject
    public SessionEndedRequestHandler() {
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(SessionEndedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        logger.debug("Handle SessionEndedRequest.");
        //any cleanup logic goes here
        return input.getResponseBuilder().build();
    }
}
