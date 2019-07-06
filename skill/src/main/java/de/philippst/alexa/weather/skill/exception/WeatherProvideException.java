package de.philippst.alexa.weather.skill.exception;

public class WeatherProvideException extends RuntimeException {

    public WeatherProvideException(String message) {
        super(message);
    }
    public WeatherProvideException(String message, Throwable cause) {
        super(message, cause);
    }

}