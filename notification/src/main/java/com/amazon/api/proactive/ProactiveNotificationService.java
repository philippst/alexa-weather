package com.amazon.api.proactive;

import com.amazon.api.auth.AwsBearerInterceptor;
import com.amazon.api.proactive.model.ProactiveEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProactiveNotificationService {

    private final Logger logger = LoggerFactory.getLogger(ProactiveNotificationService.class);

    private static final String ALEXA_EVENTS_ENDPOINT = System.getenv("ALEXA_EVENTS_API_ENDPOINT");
    private static final MediaType JSON_MEDIATYPE = MediaType.get("application/json");

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public ProactiveNotificationService() {
        AwsBearerInterceptor awsBearerInterceptor = new AwsBearerInterceptor();

        objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(awsBearerInterceptor)
                .build();
    }

    public void sendNotification(ProactiveEvent proactiveEvent) throws IOException {

        String jsonBody = objectMapper.writeValueAsString(proactiveEvent);
        RequestBody body = RequestBody.create(JSON_MEDIATYPE, jsonBody);
        logger.debug("Sending ProactiveNotification Event: referenceId={}",proactiveEvent.getReferenceId());
        logger.debug("Sending ProactiveNotification Event: body='{}'",jsonBody);

        Request request = new Request.Builder()
                .url(ALEXA_EVENTS_ENDPOINT)
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            if(response.code() == 200 || response.code() == 202){
                logger.debug("Successfully sent ProactiveEvent to Alexa: status={} response={}",response.code(),responseBody);
            } else {
                logger.error("Error sending ProactiveEvent to Alexa: status={} responseBody={}",response.code(),responseBody);
                throw new IOException();
            }
        }


    }

}
