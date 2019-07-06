package de.philippst.alexa.weather.skill.service;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.mysql.cj.jdbc.MysqlDataSource;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;

@Module
public class ServiceModule {

    @Provides
    Gson provideGson(){
        return new GsonBuilder()
                .registerTypeAdapterFactory(new GeometryAdapterFactory())
                .create();
    }

    @Provides
    GeoApiContext provideGeoApiContext(){
        return new GeoApiContext.Builder()
                .apiKey(System.getenv("GOOGLE_APIKEY"))
                .build();
    }

    @Provides
    DataSource provideMysqlDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://"+System.getenv("RDS_ENDPOINT")+":3306/alexa_weather?useSSL=false");
        dataSource.setUser(System.getenv("RDS_USERNAME"));
        dataSource.setPassword(System.getenv("RDS_PASSWORD"));
        return dataSource;
    }

    @Provides
    Jdbi provideJdbi(DataSource dataSource) {
        return Jdbi.create(dataSource);
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

}
