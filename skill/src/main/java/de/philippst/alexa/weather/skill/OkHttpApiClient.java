package de.philippst.alexa.weather.skill;

import com.amazon.ask.model.services.ApiClient;
import com.amazon.ask.model.services.ApiClientRequest;
import com.amazon.ask.model.services.ApiClientResponse;
import com.amazon.ask.model.services.Pair;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OkHttpApiClient implements ApiClient {

    private OkHttpClient okHttpClient;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

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
            requestBuilder.method(request.getMethod(), RequestBody.create(JSON, request.getBody()));
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
