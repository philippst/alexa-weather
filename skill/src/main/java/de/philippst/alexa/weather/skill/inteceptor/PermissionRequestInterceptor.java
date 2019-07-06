package de.philippst.alexa.weather.skill.inteceptor;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.services.ServiceException;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.model.services.deviceAddress.ShortAddress;
import com.amazon.ask.request.RequestHelper;
import de.philippst.alexa.weather.skill.exception.PermissionMissingException;
import de.philippst.alexa.weather.skill.exception.TechnicalException;
import de.philippst.alexa.weather.common.model.AlexaUser;
import de.philippst.alexa.weather.skill.service.AlexaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;

public class PermissionRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PermissionRequestInterceptor.class);

    private AlexaUserService alexaUserService;

    @Inject
    public PermissionRequestInterceptor(AlexaUserService alexaUserService) {
        this.alexaUserService = alexaUserService;
    }

    @Override
    public void process(HandlerInput input) {
        Session session = input.getRequestEnvelope().getSession();

        if(session == null){
            logger.debug("Skip PermissionRequest because request is out of session.");
            return;
        }

        String userId = RequestHelper.forHandlerInput(input).getUserId()
                .orElseThrow(() -> new TechnicalException("Skill PermissionRequestInterceptor without userId."));

        AlexaUser alexaUser = this.alexaUserService.getOrCreateById(userId);
        if(!alexaUser.getPermissionPostalcode()) this.updateLegacyUser(input, alexaUser);

        if(!alexaUser.getPermissionPostalcode()){
            logger.warn("Required permission is missing.");
            throw new PermissionMissingException("User has not authorized skill with location permissions.");
        } else {
            logger.debug("Required skill permissions are set.");
        }

        HashMap<String,Object> requestAttributes = new HashMap<>();
        requestAttributes.put("AlexaUser",alexaUser);
        input.getAttributesManager().setRequestAttributes(requestAttributes);

    }

    // double check if address permission is already set
    private void updateLegacyUser(HandlerInput input, AlexaUser alexaUser){
        logger.debug("Double check if legacy user already set address permission.");
        DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
        try{
            ShortAddress address = deviceAddressServiceClient.getCountryAndPostalCode(RequestHelper.forHandlerInput(input).getDeviceId());
            logger.debug("Received address data from user, even permission was missing. Update permission.");
            alexaUser.setPermissionPostalcode(true);
            alexaUser.setCountryCode(address.getCountryCode());
            alexaUser.setPostalCode(address.getPostalCode());
            this.alexaUserService.update(alexaUser);
        } catch (ServiceException ignored){
        }
    }

}
