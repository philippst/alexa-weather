package de.philippst.alexa.weather.skill.service;

import com.amazon.ask.model.services.deviceAddress.ShortAddress;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import de.philippst.alexa.weather.skill.exception.AddressOutOfBoundsException;
import de.philippst.alexa.weather.common.model.GeocodeCache;
import de.philippst.alexa.weather.skill.mapper.GeocodeCacheRowMapper;
import de.philippst.alexa.weather.common.model.Position;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class GeocodeService {

    private static final Logger logger = LoggerFactory.getLogger(GeocodeService.class);

    private GeoApiContext geoApiContext;
    private Jdbi jdbi;

    @Inject
    public GeocodeService(GeoApiContext geoApiContext, Jdbi jdbi) {
        this.geoApiContext = geoApiContext;
        this.jdbi = jdbi;
    }

    public Position geocode(ShortAddress shortAddress) {
        return this.geocode(shortAddress.getPostalCode()+" "+shortAddress.getCountryCode());
    }

    public Position geocode(String postalCode, String countryCode) {
        return this.geocode(postalCode+" "+countryCode);
    }

    private Position geocode(String address){
        logger.debug("Geocode request for address '{}'.",address);

        Position position = new Position();
        Optional<GeocodeCache> cacheResult = this.searchCache(address);
        if(!cacheResult.isPresent()) {
            logger.debug("Geocode cache missed for '{}'.",address);
            GeocodingResult[] results;

            try {
                results = GeocodingApi
                        .newRequest(this.geoApiContext)
                        .address(address)
                        .language("de")
                        .region("de")
                        .await();
            } catch (ApiException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }

            logger.debug("Received {} results for '{}'.",results.length,address);

            if(results.length == 0) throw new AddressOutOfBoundsException("Address not found");
            GeocodingResult geocodingResult = results[0];

            checkGeocodingResult(address,geocodingResult);

            position.setLat(results[0].geometry.location.lat);
            position.setLng(results[0].geometry.location.lng);

            AddressComponent location = this.getLocation(results[0]);
            if (location != null) position.setLocationName(getLocation(results[0]).longName);

            this.updateCache(address,geocodingResult);
        } else {
            logger.debug("Geocode cache hit for '{}'.",address);
            position.setLat(cacheResult.get().getLat());
            position.setLng(cacheResult.get().getLng());
            if(cacheResult.get().getLocationName() != null) position.setLocationName(cacheResult.get().getLocationName());
        }
        logger.debug("Geocode result: '{}' -> '{}'.",address,position);
        return position;
    }

    private void checkGeocodingResult(String address, GeocodingResult geocodingResult) throws AddressOutOfBoundsException {
        boolean regionDe = false;
        for(AddressComponent addressComponent : geocodingResult.addressComponents){
            if(addressComponent.shortName.equals("DE")){
                regionDe = true;
            }
        }

        if(!regionDe){
            logger.warn("Result for '{}' is out of bounds: {}",address,geocodingResult.formattedAddress);
            throw new AddressOutOfBoundsException("Address invalid");
        }
    }

    private AddressComponent getLocation(GeocodingResult geocodingResult){
        return Arrays.stream(geocodingResult.addressComponents)
                .filter(x -> Arrays.asList(x.types).contains(AddressComponentType.LOCALITY))
                .findAny()
                .orElse(null);
    }

    private Optional<GeocodeCache> searchCache(String address){
        logger.debug("Lookup in geocode cache for address: '{}'",address);

        return jdbi.withHandle(handle ->
                handle.createQuery("select *, ST_AsText(position) as latlng from geocode_cache where address = :address")
                        .bind("address",address)
                        .map(new GeocodeCacheRowMapper())
                        .findFirst()
        );
    }

    private void updateCache(String address, GeocodingResult geocodingResult) {
        logger.debug("Updating Geocode Cache for address: '{}'",address);
        AddressComponent location = this.getLocation(geocodingResult);

        jdbi.withHandle(handle ->
                handle.createUpdate("INSERT INTO geocode_cache (address, formatted_address, position, place_id, location_name) " +
                        "VALUES (:address,:formattedAddress,POINT(:lng,:lat),:placeId,:location)")
                        .bind("address",address)
                        .bind("formattedAddress",geocodingResult.formattedAddress)
                        .bind("lng",geocodingResult.geometry.location.lng)
                        .bind("lat",geocodingResult.geometry.location.lat)
                        .bind("placeId",geocodingResult.placeId)
                        .bind("location",(location != null ? location.longName : null))
                        .execute()
        );
    }

}
