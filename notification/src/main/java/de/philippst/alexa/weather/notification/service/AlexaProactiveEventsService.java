package de.philippst.alexa.weather.notification.service;

import com.amazon.ask.model.services.*;
import com.amazon.ask.model.services.proactiveEvents.CreateProactiveEventRequest;
import com.amazon.ask.model.services.proactiveEvents.ProactiveEventsServiceClient;
import com.amazon.ask.model.services.proactiveEvents.SkillStage;
import com.amazon.ask.util.JacksonSerializer;
import de.philippst.alexa.weather.notification.client.OkHttpApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlexaProactiveEventsService {

    private ProactiveEventsServiceClient proactiveEventsServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(AlexaProactiveEventsService.class);

    public AlexaProactiveEventsService() {

        Serializer serializer = new JacksonSerializer();

        ApiConfiguration apiConfiguration = DefaultApiConfiguration.builder()
                .withApiClient(new OkHttpApiClient())
                .withApiEndpoint(System.getenv("ALEXA_EVENTS_API_ENDPOINT"))
//                .withAuthorizationValue("fdgdg")
                .withSerializer(serializer)
                .build();

        AuthenticationConfiguration authenticationConfiguration = DefaultAuthenticationConfiguration.builder()
                .withClientId(System.getenv("ALEXA_MESSAGING_CLIENT_ID"))
                .withClientSecret(System.getenv("ALEXA_MESSAGING_CLIENT_SECRET"))
                .build();

        this.proactiveEventsServiceClient = new ProactiveEventsServiceClient(apiConfiguration,authenticationConfiguration);
    }

    public void sendNotification(CreateProactiveEventRequest createProactiveEventRequest){
        logger.info("Sending Alexa ProactiveEventRequest referenceId='{}'",
                createProactiveEventRequest.getReferenceId());
        logger.debug("CreateProactiveEventRequest: '{}'",createProactiveEventRequest.toString());
        SkillStage skillStage = SkillStage.valueOf(System.getenv("SKILL_STAGE"));
        this.proactiveEventsServiceClient.createProactiveEvent(createProactiveEventRequest,skillStage);
    }

}
