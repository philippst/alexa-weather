package de.philippst.alexa.weather.skill.exception;

public class PermissionMissingException extends RuntimeException {

    public PermissionMissingException(String message) {
        super(message);
    }
    public PermissionMissingException(String message, Throwable cause) {
        super(message, cause);
    }

}