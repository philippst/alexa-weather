package de.philippst.alexa.weather.notification.client;

import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.model.services.ApiClientRequest;
import com.amazon.ask.model.services.ApiClientResponse;
import com.amazon.ask.model.services.Pair;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OkHttpApiClient implements ApiClient {

    private OkHttpClient okHttpClient;

    private static final Logger logger = LoggerFactory.getLogger(OkHttpApiClient.class);

    public OkHttpApiClient() {
        this.okHttpClient = new OkHttpClient();
    }

    @Override
    public ApiClientResponse invoke(ApiClientRequest request) {

        Request lowLevelRequest = generateRequest(request);

        try (Response response = okHttpClient.newCall(lowLevelRequest).execute()) {
            return generateResponse(response);
        } catch (IOException e) {
            throw new RuntimeException("There was an error executing the request", e);
        }
    }

    private Request generateRequest(ApiClientRequest request) {

        Request.Builder requestBuilder = new Request.Builder().url(request.getUrl());

        if(request.getBody() != null) {
            MediaType mediaType = MediaType.get(
                    request.getHeaders()
                            .stream()
                            .filter(
                                pair -> pair.getName().toLowerCase().equals("content-type")
                            )
                            .findFirst()
                            .orElse(new Pair<>("Content-Type","application/json"))
                            .getValue()
            );

            logger.debug("API Request method='{}' media='{}' body='{}'",request.getMethod(),mediaType, request.getBody());
            requestBuilder.method(request.getMethod(), RequestBody.create(mediaType, request.getBody()));
        } else {
            requestBuilder.method(request.getMethod(), null);
        }

        if (request.getHeaders() != null) {
            for (Pair<String, String> header : request.getHeaders()) {
                requestBuilder.addHeader(header.getName(),header.getValue());
            }
        }
        return requestBuilder.build();
    }

    private ApiClientResponse generateResponse(Response lowLevelResponse) throws IOException {
        ApiClientResponse response = new ApiClientResponse();
        response.setStatusCode(lowLevelResponse.code());

        List<Pair<String,String>> headerPairs = new ArrayList<>();
        for(String name: lowLevelResponse.headers().names()){
            headerPairs.add(new Pair<>(name, lowLevelResponse.header(name)));
        }
        response.setHeaders(headerPairs);

        if (lowLevelResponse.body() != null) {
            response.setBody(lowLevelResponse.body().string());
        }

        return response;
    }


}
