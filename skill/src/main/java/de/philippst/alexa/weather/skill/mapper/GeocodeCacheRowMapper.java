package de.philippst.alexa.weather.skill.mapper;

import com.github.filosganga.geogson.model.Point;
import de.philippst.alexa.weather.common.model.GeocodeCache;
import de.philippst.alexa.weather.skill.util.GeoUtils;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GeocodeCacheRowMapper implements RowMapper<GeocodeCache> {

    private final Logger logger = LoggerFactory.getLogger(GeocodeCacheRowMapper.class);

    @Override
    public GeocodeCache map(ResultSet rs, StatementContext ctx) throws SQLException {
        Point position = GeoUtils.wktToPoint(rs.getString("latlng"));
        GeocodeCache geocodeCache = new GeocodeCache();
        geocodeCache.setAddress(rs.getString("address"));
        geocodeCache.setFormattedAddress(rs.getString("formatted_address"));
        geocodeCache.setLat(position.lat());
        geocodeCache.setLng(position.lon());
        geocodeCache.setPlaceId(rs.getString("place_id"));
        geocodeCache.setLocationName(rs.getString("location_name"));
        return geocodeCache;
    }
}
