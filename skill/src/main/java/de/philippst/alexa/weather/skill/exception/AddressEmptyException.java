package de.philippst.alexa.weather.skill.exception;

public class AddressEmptyException extends AddressException {

    public AddressEmptyException(String message, Exception e) {
        super(message, e);
    }

    public AddressEmptyException(String message) {
        super(message);
    }
}
