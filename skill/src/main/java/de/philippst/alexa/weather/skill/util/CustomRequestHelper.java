package de.philippst.alexa.weather.skill.util;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Device;
import com.amazon.ask.util.ValidationUtils;

import java.util.Optional;

/**
 * Based on alexa-skills-kit-sdk-for-java/ask-sdk-core/src/com/amazon/ask/request/RequestHelper.java
 */
public class CustomRequestHelper {

    private final HandlerInput handlerInput;

    private CustomRequestHelper(HandlerInput handlerInput) {
        this.handlerInput = ValidationUtils.assertNotNull(handlerInput, "handlerInput");
    }

    public static CustomRequestHelper forHandlerInput(HandlerInput input) {
        return new CustomRequestHelper(input);
    }

    /**
     * Retrieves the optional device ID from the request.
     *
     * The method retrieves the deviceId property from the input request. This value uniquely identifies the device
     * and is generally used as input for some Alexa-specific API calls. More information about this can be found here:
     * https://developer.amazon.com/docs/custom-skills/request-and-response-json-reference.html#system-object
     *
     * @return an {@link Optional} containing the device ID
     */
    public Optional<String> getDeviceId() {
        return Optional.ofNullable(handlerInput.getRequestEnvelope().getContext().getSystem().getDevice())
                .map(Device::getDeviceId);
    }

}
