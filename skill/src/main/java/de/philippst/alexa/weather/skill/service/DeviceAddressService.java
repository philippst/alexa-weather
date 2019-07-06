package de.philippst.alexa.weather.skill.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.model.services.deviceAddress.ShortAddress;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class DeviceAddressService {

    @Inject
    public DeviceAddressService() { }

    private final Logger logger = LoggerFactory.getLogger(DeviceAddressService.class);

    public ShortAddress getShortAddress(HandlerInput input){
        DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
        String deviceId =
                Optional.ofNullable(RequestHelper.forHandlerInput(input).getDeviceId()).orElseThrow(() -> new TechnicalException("Calling address service without device id"));

        logger.debug("Requesting shortAddress from Alexa deviceAddressService for deviceId: {}",deviceId);
        ShortAddress shortAddress = deviceAddressServiceClient.getCountryAndPostalCode(deviceId);
        logger.debug("Received shortAddress from Alexa deviceAddressService: {}/{}",
                shortAddress.getCountryCode(),shortAddress.getPostalCode());
        return shortAddress;
    }

}
