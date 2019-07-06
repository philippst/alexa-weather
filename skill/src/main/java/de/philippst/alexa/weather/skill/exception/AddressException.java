package de.philippst.alexa.weather.skill.exception;

public class AddressException extends RuntimeException {

    public AddressException(String message, Throwable e) {
        super(message, e);
    }

    public AddressException(String message) {
        super(message);
    }
}