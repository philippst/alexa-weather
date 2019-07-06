package de.philippst.alexa.weather.skill.service;

import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import de.philippst.alexa.weather.common.dwd.Alert;
import de.philippst.alexa.weather.common.dwd.Severity;
import de.philippst.alexa.weather.skill.exception.WeatherProvideException;
import de.philippst.alexa.weather.common.model.Position;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherAlertService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherAlertService.class);

    private Gson gson;
    private OkHttpClient okHttpClient;

    @Inject
    public WeatherAlertService(Gson gson, OkHttpClient okHttpClient) {
        this.gson = gson;
        this.okHttpClient = okHttpClient;
    }

    public List<Alert> getAlertsByPosition(Position position){

        HttpUrl httpUrl = this.buildUri(position);

        logger.debug("Request WeatherAlerts from dwd via uri: {}",httpUrl);

        Request request = new Request.Builder().url(httpUrl).build();

        try (Response resp = okHttpClient.newCall(request).execute()) {
            String response = resp.body().string();
            FeatureCollection featureCollection = gson.fromJson(response, FeatureCollection.class);
            List<Feature> featureList = featureCollection.features();

            List<Alert> alerts = new ArrayList<>();
            for(Feature feature : featureList){
                alerts.add(this.featureToAlert(feature.properties()));
            }
            logger.info("Received WeatherAlerts from dwd for position {}: {}",position,featureList.size());

            return alerts;

        } catch (IOException e) {
            throw new WeatherProvideException("Connection weather data provider failed.",e);
        }
    }

    private Alert featureToAlert(Map<String, JsonElement> map){
        Alert alert = new Alert();
        alert.setIdentifier(map.get("IDENTIFIER").getAsString());
        alert.setSender(map.get("SENDER").getAsString());
        alert.setSent(map.get("SENT").getAsString());
        alert.setStatus(map.get("STATUS").getAsString());
        alert.setMsgType(map.get("MSGTYPE").getAsString());
        alert.setHeadline(map.get("HEADLINE").getAsString());
        alert.setDescription(map.get("DESCRIPTION").getAsString());
        alert.setExpires(ZonedDateTime.parse(map.get("EXPIRES").getAsString()));
        alert.setEffective(ZonedDateTime.parse(map.get("EFFECTIVE").getAsString()));
        alert.setOnset(ZonedDateTime.parse(map.get("ONSET").getAsString()));
        alert.setSeverity(Severity.valueOf(map.get("SEVERITY").getAsString().toUpperCase()));
        if(!map.get("INSTRUCTION").isJsonNull()) alert.setInstruction(map.get("INSTRUCTION").getAsString());
        return alert;
    }

    private HttpUrl buildUri(Position position){
        return new HttpUrl.Builder()
                .scheme("https")
                .host("maps.dwd.de")
                .addPathSegments("/geoserver/dwd/ows")
                .addQueryParameter("service", "WFS")
                .addQueryParameter("version", "2.0.0")
                .addQueryParameter("request", "GetFeature")
                .addQueryParameter("typeName", "dwd:Warnungen_Gemeinden")
                .addQueryParameter("outputFormat","application/json")
                .addQueryParameter("bbox",position.getBbox())
                .build();
    }

    public List<Alert> removeDuplicates(List<Alert> alertList){
        List<Alert> results = new ArrayList<>();
        for(Alert alert : alertList) {
            if(!(results.contains(alert)))
            {
                results.add(alert);
            }
        }
        return results;
    }

}
