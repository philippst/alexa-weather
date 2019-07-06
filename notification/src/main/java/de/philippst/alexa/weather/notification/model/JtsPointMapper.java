package de.philippst.alexa.weather.notification.model;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JtsPointMapper implements ColumnMapper<Point> {
    public Point map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        WKTReader wktReader = new WKTReader();
        try {
            return (Point) wktReader.read(r.getString(columnNumber));
        } catch (ParseException e) {
            throw new SQLException("Can not cast to JTS POINT.");
        }
    }
}