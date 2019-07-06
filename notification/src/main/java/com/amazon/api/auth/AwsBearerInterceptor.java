package com.amazon.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AwsBearerInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(AwsBearerInterceptor.class);

    private AwsToken bearerToken;

    private static final String CLIENT_ID = System.getenv("ALEXA_MESSAGING_CLIENT_ID").trim();
    private static final String CLIENT_SECRET = System.getenv("ALEXA_MESSAGING_CLIENT_SECRET").trim();
    private static final String AMAZON_AUTH_ENDPOINT = "https://api.amazon.com/auth/O2/token";
    private static final MediaType X_WWW_FORM = MediaType.get("application/x-www-form-urlencoded;charset=UTF-8");

    private AwsToken requestToken() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient();

        String requestBody = String.format(
                "grant_type=client_credentials&client_id=%s&client_secret=%s&scope=alexa::proactive_events",
                CLIENT_ID,
                CLIENT_SECRET
        );

        logger.debug("Requesting bearer token: endpoint='{}' body='{}'",AMAZON_AUTH_ENDPOINT,requestBody);

        RequestBody body = RequestBody.create(X_WWW_FORM, requestBody);
        Request request = new Request.Builder()
                .url(AMAZON_AUTH_ENDPOINT)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 200){
                String responseBody = response.body().string();
                logger.info("Received bearer token from Amazon: {}",responseBody);
                return mapper.readValue(responseBody,AwsToken.class);
            } else {
                logger.error("Alexa Messaging Authentication failed. Response Status Code: {}",response.code());
                throw new IOException();
            }
        }
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        if(this.bearerToken == null){
            logger.debug("No bearer token available. Requesting token.");
            this.bearerToken = this.requestToken();
        }

        Request newRequest = request.newBuilder()
                .addHeader("Authorization","Bearer "+this.bearerToken.getAccessToken())
                .build();

        return chain.proceed(newRequest);
    }
}