package de.philippst.alexa.weather.skill.exception;

public class AddressOutOfBoundsException extends AddressException {

    public AddressOutOfBoundsException(String message, Exception e) {
        super(message, e);
    }

    public AddressOutOfBoundsException(String message) {
        super(message);
    }

}