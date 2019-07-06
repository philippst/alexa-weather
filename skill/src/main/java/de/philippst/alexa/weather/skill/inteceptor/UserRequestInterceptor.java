package de.philippst.alexa.weather.skill.inteceptor;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.services.deviceAddress.ShortAddress;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.common.model.Position;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import de.philippst.alexa.weather.skill.service.DeviceAddressService;
import de.philippst.alexa.weather.skill.service.GeocodeService;
import de.philippst.alexa.weather.skill.util.CustomRequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;

public class UserRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserRequestInterceptor.class);

    private AlexaUserService alexaUserService;
    private DeviceAddressService deviceAddressService;
    private GeocodeService geocodeService;

    @Inject
    public UserRequestInterceptor(AlexaUserService alexaUserService, DeviceAddressService deviceAddressService, GeocodeService geocodeService) {
        this.alexaUserService = alexaUserService;
        this.deviceAddressService = deviceAddressService;
        this.geocodeService = geocodeService;
    }

    @Override
    public void process(HandlerInput input) {

        Session session = input.getRequestEnvelope().getSession();

        if(session == null){
            logger.debug("Skip UserRequestInterceptor because request is out of session.");
            return;
        }

        AlexaUser alexaUser = (AlexaUser) input.getAttributesManager().getRequestAttributes().get("AlexaUser");

        logger.debug("Receiving user request: deviceId {}, lng {}, lat {}, postalcode {}",alexaUser.getDeviceId(),
                alexaUser.getLng(),alexaUser.getLat(),alexaUser.getPostalCode());
        this.completeAlexaUser(alexaUser,input);
    }

    private void completeAlexaUser(AlexaUser alexaUser, HandlerInput input){

        if(alexaUser.getDeviceId() == null || alexaUser.getLng() == null || alexaUser.getLat() == null || alexaUser.getPostalCode() == null){

            ShortAddress shortAddress = this.deviceAddressService.getShortAddress(input);
            Position position = this.geocodeService.geocode(shortAddress);

            alexaUser.setDeviceId(
                    CustomRequestHelper.forHandlerInput(input).getDeviceId().orElseThrow(
                            () -> new TechnicalException("UserRequest without deviceId"))
            );
            alexaUser.setLat(position.getLat());
            alexaUser.setLng(position.getLng());
            alexaUser.setPostalCode(shortAddress.getPostalCode());
            alexaUser.setCountryCode(shortAddress.getCountryCode());
            alexaUser.setLocationName(position.getLocationName());
            alexaUser.setDeviceId(CustomRequestHelper.forHandlerInput(input).getDeviceId().get());
            alexaUser.setLastUpdated(new Date());

            this.alexaUserService.update(alexaUser);

            HashMap<String,Object> requestAttributes = new HashMap<>();
            requestAttributes.put("AlexaUser",alexaUser);
            input.getAttributesManager().setRequestAttributes(requestAttributes);

        }
    }

}